package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.UserService;
import service.UserServiceImpl;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService us = new UserServiceImpl();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		System.out.println(id);
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.println("입력하신 ID :" + id);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String command = request.getParameter("command");
		System.out.println(command);
		if (command.equals("login")) {
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			Map<String, String> hm = us.getUserLogin(id, pwd);
			String result = "로그인실패";
			if (hm != null) {
				HttpSession session = request.getSession();
				session.setAttribute("user", hm);
				// session.setAttribute("user_no", hm.get("user_no"));
				// session.setAttribute("name", hm.get("name"));
				// session.setAttribute("hobby", hm.get("hobby"));

				result = "로그인성공";
				response.sendRedirect("/main.jsp");
			}
		} else if (command.equals("signin")) {
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String[] hobbies = request.getParameterValues("hobby");
			String hobby = "";
			for (String h : hobbies) {
				hobby += h + ",";
			}
			hobby = hobby.substring(0, hobby.length() - 1);
			Map<String, String> hm = new HashMap<String, String>();
			hm.put("id", id);
			hm.put("pwd", pwd);
			hm.put("name", name);
			hm.put("hobby", hobby);

			String result = " 회원가입에 실패하셨습니다.";
			int rCnt = us.insertUser(hm);
			if (rCnt == 1) {
				result = "<script>";
				result += "alert('회원가입에 성공하셨습니다. 로그인해주시기 바랍니다');";
				result += "location.href='/login.jsp'";
				result += "</script>";
			}
			doProcess(response, result);
		} else if (command.equals("logout")) {
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect("/login.jsp");
		} else if (command.equals("modify")) {
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String[] hobbies = request.getParameterValues("hobby");
			String hobby = "";
			for (String h : hobbies) {
				hobby += h + ",";
			}
			hobby = hobby.substring(0, hobby.length() - 1);
			Map<String, String> hm = new HashMap<String, String>();
			hm.put("id", id);
			hm.put("pwd", pwd);
			hm.put("name", name);
			hm.put("hobby", hobby);
			HttpSession session = request.getSession();
			Map<String, String> user = (Map) session.getAttribute("user");
			hm.put("user_no", user.get("user_no"));
			int rCnt = us.updateUser(hm);
			String result = " 회원정보수정 실패하셨습니다.";
			if (rCnt == 1) {
				session.invalidate();
				result = "<script>";
				result += "alert('회원수정에 성공하셨습니다. 로그인해주시기 바랍니다');";
				result += "location.href='/login.jsp'";
				result += "</script>";
			}
			doProcess(response, result);
		} else if (command.equals("delete")) {
			Map<String, String> hm = new HashMap<String, String>();
			HttpSession session = request.getSession();
			Map<String, String> user = (Map) session.getAttribute("user");
			hm.put("user_no", user.get("user_no"));
			int rCnt = us.deleteUser(hm);
			String result = " 회원탈퇴 실패하셨습니다.";
			if (rCnt == 1) {
				session.invalidate();
				result = "<script>";
				result += "alert('회원탈퇴에 성공하셨습니다. 로그인해주시기 바랍니다');";
				result += "location.href='/login.jsp'";
				result += "</script>";

			}
			doProcess(response, result);
		} else if (command.equals("list")) {
			Map<String, String> hm = new HashMap<String, String>();
			hm.put("name", request.getParameter("name"));
			List<Map<String, String>> userList = us.getUserList(hm);
			String result = "<table border='1'>";
			for (Map<String, String> m : userList) {
				result += "<tr>";
				result += "<td>" + m.get("user_no") + "</td>";
				result += "<td>" + m.get("id") + "</td>";
				result += "<td>" + m.get("name") + "</td>";
				result += "<td>" + m.get("hobby") + "</td>";
				result += "<td><input type='button' value='수정'></td>";
				result += "<td><input type='button' value='삭제'></td>";
				result += "</tr>";
			}
			result += "</table>";
			doProcess(response, result);
		}

	}

	public void doProcess(HttpServletResponse response, String result) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.println(result);
	}

}
