package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Order;
import bean.OrderItem;
import bean.Product;
import bean.User;
import exception.MsgException;
import utils.JDBCUtils;

/**
 * Servlet implementation class OrderAddServlet
 */
@WebServlet("/OrderAddServlet")
public class OrderAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderAddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//解决乱码请求
		ServletContext context = this.getServletContext();
		String encode = context.getInitParameter("encode");
		request.setCharacterEncoding(encode);
		
		//解决响应乱码
		response.setContentType("text/html;charset=" + encode);
		
		//1.获取当前用户
		User user=(User)request.getSession().getAttribute("user");
		if(user==null) {
			response.getWriter().write("<a href='"+request.getContextPath()+
					"/EasyMallPage/login/login.jsp'>请先登录！</a>");
		}else {
			//2.获取订单数据，并将其加载入Javabean中
			Order order = new Order();
			order.setId(UUID.randomUUID().toString());
			order.setPaystate(0);//0表示默认未支付
			order.setReceiverinfo(request.getParameter("receiverinfo"));
			order.setUser_id(user.getId());
			/*
			 * 创建list集合保存一个订单的所有订单项目，一个订单想可以包含多个商品，
			 * 一个商品对应一个订单项（OrderItem）*/
			List<OrderItem>list = new ArrayList<OrderItem>();
			
			//计算订单金额，以数据库中的信息为准
			double totalMoney = 0;
			Map<Product, Integer> map = (Map<Product,Integer>)request
					.getSession().getAttribute("cartmap");
			for (Map.Entry<Product, Integer> entry : map.entrySet()) {
				double price = entry.getKey().getPrice();// 当前商品的单价
				int buyNum = entry.getValue();// 购买数量
				totalMoney += price * buyNum;// 计算订单总金额
				OrderItem item = new OrderItem();
				item.setOrder_id(order.getId());
				item.setProduct_id(entry.getKey().getId());
				item.setBuynum(buyNum);
				// 将订单中的每一个商品即每一个订单项添加到一个list集合中保存
				list.add(item);
			}
			order.setMoney(totalMoney);// 将计算好的订单金额存入javabean中
			// 5.调用addOrder方法添加订单
			try {
				addOrder(order, list);
			} catch (MsgException e) {
				response.getWriter().write(
						"<h1 style='color:red;text-align:center'>"
								+ e.getMessage() + "</h1>");
				return;
			}
			//订单添加后清空购物车中的商品
			map.clear();
			
			//添加成功后回到订单列表页面
			response.sendRedirect(request.getContextPath()+"/servlet/OrderListServlet");
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public void addOrder(Order order, List<OrderItem> list) throws MsgException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "insert into orders(id,money,receiverinfo,paystate,user_id) values(?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, order.getId());
			ps.setDouble(2, order.getMoney());
			ps.setString(3, order.getReceiverinfo());
			ps.setInt(4, order.getPaystate());
			ps.setInt(5, order.getUser_id());
			ps.executeUpdate();
			JDBCUtils.close(conn, ps, null);
			for (OrderItem orderItem : list) {
				// 检查购买数量(orderItem.buyNum)是否小于等于库存数量(Product.pnum)
				// 获取购买数量
				int buyNum = orderItem.getBuynum();
				// 获取库存数量
				// >>获取商品id
				String pid = orderItem.getProduct_id();
				// >>查询商品信息
				Product prod = findProdById(pid);
				int pnum = prod.getPnum();
				if (buyNum > pnum) {// 如果购买数量大于库存数量
					throw new MsgException("库存数量不足, 订单添加失败!");
				}
				addOrderItem(orderItem);
				updatePnum(pid, prod.getPnum() - buyNum);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//3.	findProdById方法
public Product findProdById(String pid) {
		Product prod = new Product();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "select * from products where id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, pid);
			rs = ps.executeQuery();
			if (rs.next()) {
				prod.setId(rs.getString("id"));
				prod.setName(rs.getString("name"));
				prod.setPnum(rs.getInt("pnum"));
				prod.setDescription(rs.getString("description"));
				prod.setImgurl(rs.getString("imgurl"));
				prod.setPrice(rs.getDouble("price"));
				prod.setCategory(rs.getString("category"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, rs);
		}
		return prod;
	}

//4.	addOrderItem方法
public void addOrderItem(OrderItem orderItem) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "insert into orderitem(order_id,product_id,buynum) values(?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, orderItem.getOrder_id());
			ps.setString(2, orderItem.getProduct_id());
			ps.setInt(3, orderItem.getBuynum());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, null);
		}
	}
//5.	updatePnum方法
public void updatePnum(String pid, int num) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "update products set pnum = ? where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			ps.setString(2, pid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, null);
		}
	}


}
