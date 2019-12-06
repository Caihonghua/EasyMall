package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Product;
import utils.JDBCUtils;

/**
 * Servlet implementation class ProInfoServlet
 */
@WebServlet("/ProInfoServlet")
public class ProInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//获取商品的id
		String pid=request.getParameter("pid");
		//根据商品的id查询商品心虚
		Product product=new Product();
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try {
			conn=JDBCUtils.getConn();
			String sql="select * from products where id=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, pid);
			rs=ps.executeQuery();
			if(rs.next()) {
				product.setId(rs.getString("id"));
				product.setName(rs.getString("name"));
				product.setCategory(rs.getString("category"));
				product.setDescription(rs.getString("description"));
				product.setPrice(rs.getDouble("price"));
				product.setPnum(rs.getInt("pnum"));
				product.setImgurl(rs.getString("imgurl"));
			}
			
			request.setAttribute("prod", product);
			request.getRequestDispatcher("/EasyMallPage/product/prod_info.jsp")
			.forward(request, response);
			System.out.println(request.getContextPath());
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn, ps, rs);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
