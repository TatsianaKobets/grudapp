package org.example.grudapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.grudapp.model.User;
import org.example.grudapp.service.UserService;

@WebServlet("/api/users")
public class UserServlet extends HttpServlet {

  private final UserService userService = new UserService();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Получаем список пользователей в формате JSON
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    out.print(objectMapper.writeValueAsString(userService.getUsers()));
    out.flush();
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Создание нового пользователя
    User user = objectMapper.readValue(request.getInputStream(), User.class);
    userService.registerUser(user.getEmail(), user.getPassword(), user.getName());

    response.setStatus(HttpServletResponse.SC_CREATED);
    PrintWriter out = response.getWriter();
    out.print(objectMapper.writeValueAsString(user));
    out.flush();
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Обновление информации о пользователе
    User user = objectMapper.readValue(request.getInputStream(), User.class);
    userService.editUserProfile(user);

    response.setStatus(HttpServletResponse.SC_OK);
    PrintWriter out = response.getWriter();
    out.print(objectMapper.writeValueAsString(user));
    out.flush();
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Удаление пользователя по email
    String email = request.getParameter("email");
    userService.deleteUser(email);

    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}