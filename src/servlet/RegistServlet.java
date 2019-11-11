package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.JDBCUtils;

/**
 * Servlet implementation class RegistServlet
 */
@WebServlet("/RegistServlet")
public class RegistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.获取ServletContext初始化参数

		//String encode = this.getServletContext().getInitParameter("encode");
		//2.解决乱码问题
		//request.setCharacterEncoding(encode);
		//response.setContentType("text/html;charset="+encode);
		//System.out.println(request.getContextPath());
		
		//3.获取请求参数		
		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		String password2 = request.getParameter("password2").trim();
		String nickname = request.getParameter("nickname").trim();
		String email = request.getParameter("email").trim();
		String valistr = request.getParameter("valistr").trim();
		
		//4.校验数据
		if(username == null || "".equals(username)){
			request.setAttribute("msg","用户名不能为空！");
			//转发回注册页面进行提示
			request.getRequestDispatcher("/EasyMallPage/regist/regist.jsp").forward(request, response);
			return;
		}
		if(password == null || "".equals(password)){
			request.setAttribute("msg","密码不能为空！");
			//转发回注册页面进行提示
			request.getRequestDispatcher("/EasyMallPage/regist/regist.jsp").forward(request, response);
			return;
		}
		if(!password.equals(password2)){
			request.setAttribute("msg", "两次密码输入不一致");
			request.getRequestDispatcher("/EasyMallPage/regist/regist.jsp").forward(request, response);
			return;
		}
		if(email == null || "".equals(email)){
			request.setAttribute("msg","邮箱地址不能为空！");
			//转发回注册页面进行提示
			request.getRequestDispatcher("/EasyMallPage/regist/regist.jsp").forward(request, response);
			return;
		}
		if(!email.matches("^\\w+@\\w+(\\.\\w+)+$")){
			request.setAttribute("msg", "邮箱地址格式错误");
			request.getRequestDispatcher("/EasyMallPage/regist/regist.jsp").forward(request, response);
			return;
		}
		if( valistr== null || "".equals(valistr)){
			request.setAttribute("msg","验证码不能为空！");
			//转发回注册页面进行提示
			request.getRequestDispatcher("/EasyMallPage/regist/regist.jsp").forward(request, response);
			return;
		}
		//进行验证码校验
		String code=(String)request.getSession().
				getAttribute("yzm");
		if(!valistr.equalsIgnoreCase(code)) {
			request.setAttribute("msg", "验证码错误");
			request.getRequestDispatcher("/EasyMallPage/regist"
					+ "/regist.jsp").forward(request, response);
			return;
		}
		//检验表单是否被重复提交
		String token1=request.getSession().getAttribute("token").toString();
		String token2=request.getParameter("token");
		if(token1==null||token2==null||!token1.equals(token2)) {
			throw new RuntimeException("不要重复提交表单");
		}else {
			request.getSession().removeAttribute("token");
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConn();
			String sql = "select * from user where username=?";
		    ps =conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next()){
				request.setAttribute("msg","该用户名已被注册！");
				request.getRequestDispatcher("/EasyMallPage/regist/regist.jsp").forward(request, response);
				return;
			}else{
				
				ps = conn.prepareStatement("set names utf8");
				ps.executeQuery();
				sql = "insert into user values(null,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, username);			
				ps.setString(2, password);		
				ps.setString(3, nickname);		
				ps.setString(4, email);
				ps.executeUpdate();
				//注册成功给出提示信息, 3秒之后跳转到首页
				response.getWriter().write("<h1 style='text-align:center; color:red;'>恭喜您, 注册成功! 3秒之后跳转到首页...</h1>");
				response.setHeader("refresh", "3;url="+request.getContextPath()+"/EasyMallPage/index/index.jsp");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.close(conn, ps, rs);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
