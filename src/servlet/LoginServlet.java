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

import bean.User;
import utils.JDBCUtils;

/**
 * Servlet implementation class LoginSevlet
 */
@WebServlet("/LoginSevlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		//从login.jsp中获取表单数据
		String username=request.getParameter("username").trim();
		String password=request.getParameter("password").trim();
		//判断用户名和密码是否为空
		if(username==null||"".equals(username)) {
			request.setAttribute("msg","用户名不能为空！");
			//转发回注册页面进行提示
			request.getRequestDispatcher("/EasyMallPage/login/login.jsp").forward(request, response);
			return;
		}
		if(password==null||"".equals(password)) {
			request.setAttribute("msg","密码不能为空！");
			//转发回注册页面进行提示
			request.getRequestDispatcher("/EasyMallPage/login/login.jsp").forward(request, response);
			return;
		}
		
		//判断用户名和密码是否匹配
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=JDBCUtils.getConn();
			String sql="select * from user where username=? and password=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			rs=ps.executeQuery();
			if(!rs.next()) {
				request.setAttribute("msg","用户名或密码错误!");
				//转发回注册页面进行提示
				request.getRequestDispatcher("/EasyMallPage/login/login.jsp").forward(request, response);
				return;
			}else {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				request.getSession().setAttribute("user", user);
				response.sendRedirect(request.getContextPath()+"/EasyMallPage/index/index.jsp");
			}
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
