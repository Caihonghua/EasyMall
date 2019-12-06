package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Order;
import bean.OrderInfo;
import bean.OrderItem;
import bean.Product;
import bean.User;
import utils.JDBCUtils;

/**
 * Servlet implementation class OrderListServlet
 */
@WebServlet("/OrderListServlet")
public class OrderListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderListServlet() {
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
			List<OrderInfo> orderInfoList=findOrderInfoByUserId(user.getId());
			request.setAttribute("list", orderInfoList);
			request.getRequestDispatcher("/EasyMallPage/orderlist/order_list.jsp")
			.forward(request, response);
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public List<OrderInfo> findOrderInfoByUserId(int userId) {
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		// 1.根据用户id查询该用户的所有订单信息，查询orders表
		List<Order> orderList = findOrderByUserId(userId);
		//2.遍历每一个订单, 通过订单id查询当前订单中包含的所有订单项信息
		for (Order order : orderList) {
			String orderId = order.getId();
			//根据用户order_id查询该订单号的所有订单项信息，查询orderitem表
			List<OrderItem> orderItems = findOrderItemByOrderId(orderId);
			//3.遍历每一个订单项, 通过订单项获取商品信息及商品的购买数量
			Map<Product, Integer> map = new HashMap<Product, Integer>();
			for (OrderItem orderItem : orderItems) {
				//3.1.获取商品id, 通过商品id查询商品信息, 返回Product对象
				Product product = finProductById(orderItem.getProduct_id());
				//3.2.将商品信息和购买数量存入map中
				map.put(product, orderItem.getBuynum());
			}
			//4.将订单信息和所有的订单项信息存入OrderInfo中
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrder(order);
			orderInfo.setMap(map);
			//5.将一个完整的订单信息存入List集合中
			orderInfoList.add(orderInfo);
		}
		return orderInfoList;
	}

//1.3.5	findOrderByUserId()方法:根据用户id查询该用户的所有订单信息
public List<Order> findOrderByUserId(int userId) {
		List<Order> orderList = new ArrayList<Order>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "select * from orders where user_id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Order order = new Order();
				order.setId(rs.getString("id"));
				order.setMoney(rs.getDouble("money"));
				order.setReceiverinfo(rs.getString("receiverinfo"));
				order.setPaystate(rs.getInt("paystate"));
				order.setUser_id(rs.getInt("user_id"));
				order.setOrdertime(rs.getTimestamp("ordertime"));
				orderList.add(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, rs);
		}
		return orderList;
	}
//1.3.6	findOrderItemByOrderId()方法
//根据用户order_id查询该订单号的所有订单项信息，查询orderitem表
public List<OrderItem> findOrderItemByOrderId(String orderId) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "select * from orderItem where order_id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			while (rs.next()) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder_id(orderId);
				orderItem.setProduct_id(rs.getString("product_id"));
				orderItem.setBuynum(rs.getInt("buynum"));
				orderItems.add(orderItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, rs);
		}
		return orderItems;
	}
//1.3.7	finProductById()方法
//3.根据用户商品id 查询该订单号的所购买的商品信息，查询products表
public Product finProductById(String id) {
		Product product = new Product();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "select * from products where id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				product.setId(id);
				product.setName(rs.getString("name"));
				product.setCategory(rs.getString("category"));
				product.setDescription(rs.getString("description"));
				product.setImgurl(rs.getString("imgurl"));
				product.setPnum(rs.getInt("pnum"));
				product.setPrice(rs.getDouble("price"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(conn, ps, rs);
		}
		return product;
	}

}
