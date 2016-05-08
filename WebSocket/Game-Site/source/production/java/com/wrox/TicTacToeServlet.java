package com.wrox;

import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *	游戏的入口servlet
 */
@WebServlet(name = "ticTacToeServlet", urlPatterns = "/ticTacToe")
public class TicTacToeServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 请求中放入游戏列表
		request.setAttribute("pendingGames", TicTacToeGame.getPendingGames());
		// 跳转页面到list，显示当前所有的游戏
		this.view("list", request, response);
	}

	@Override
	// 在list页面中，点击事件提交到此
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		// action如果是join表示是想加入到指定游戏中
		if ("join".equalsIgnoreCase(action)) {
			String gameIdString = request.getParameter("gameId");
			String username = request.getParameter("username");
			if (username == null || gameIdString == null || !NumberUtils.isDigits(gameIdString))
				this.list(request, response);
			else {
				request.setAttribute("action", "join");
				request.setAttribute("username", username);
				request.setAttribute("gameId", Long.parseLong(gameIdString));
				this.view("game", request, response);
			}
		} else if ("start".equalsIgnoreCase(action)) {
			String username = request.getParameter("username");
			if (username == null)
				this.list(request, response);
			else {
				request.setAttribute("action", "start");
				request.setAttribute("username", username);
				request.setAttribute("gameId", TicTacToeGame.queueGame(username));
				this.view("game", request, response);
			}
		} else
			this.list(request, response);
	}

	private void view(String view, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/view/ticTacToe/" + view + ".jsp").forward(request, response);
	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/ticTacToe"));
	}
}
