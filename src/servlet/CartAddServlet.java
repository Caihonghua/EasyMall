package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Product;
import utils.JDBCUtils;


@WebServlet("/CartAddServlet")
public class CartAddServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1.获取商品的id和购买数量
		String pid = request.getParameter("pid");
		Integer buyNum = Integer.parseInt(request.getParameter("buyNum"));
		System.out.println(buyNum);
		// 2根据商品id查询商品信息
				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					conn = JDBCUtils.getConn();
					String sql = "select * from products where id =?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, pid);
					rs = ps.executeQuery();
					if (rs.next()) {
						Product prod = new Product();
						prod.setId(rs.getString("id"));
						prod.setName(rs.getString("name"));
						prod.setPrice(rs.getDouble("price"));
						prod.setCategory(rs.getString("category"));
						prod.setPnum(rs.getInt("pnum"));
						prod.setImgurl(rs.getString("imgurl"));
						prod.setDescription(rs.getString("description"));
						Map<Product, Integer> map = (HashMap<Product, Integer>)request.getSession().getAttribute("cartmap");
						
						//如果商品的购买数量小于0 则在购物车中删除该商品
						System.out.println(map.containsKey(prod));
						if(buyNum < 0){
							map.remove(prod);
						}else{
							map.put(prod, map.containsKey(prod) ? map.get(prod) + buyNum : buyNum);
						}			
						response.sendRedirect(request.getContextPath() + "/EasyMallPage/cart/cart.jsp");
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException();
				} finally {
					JDBCUtils.close(conn, ps, rs);
				}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
