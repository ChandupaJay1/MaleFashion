package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(request.getReader(), JsonObject.class);
        String verification = dto.get("verification").getAsString();

        if (request.getSession().getAttribute("email") != null) {

            String email = request.getSession().getAttribute("email").toString();

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));
            criteria.add(Restrictions.eq("verification", verification));

            if (!criteria.list().isEmpty()) {

                User user = (User) criteria.uniqueResult();
                user.setVerification("Verified");

                session.update(user);
                session.beginTransaction().commit();

                User_DTO user_DTO = new User_DTO();
                user_DTO.setFirst_name(user.getFirst_name());
                user_DTO.setLast_name(user.getLast_name());
                user_DTO.setEmail(user.getEmail());

                request.getSession().removeAttribute("email");
                request.getSession().setAttribute("user", user_DTO);

                response_DTO.setSuccess(true);
                response_DTO.setContent("Verification success");

            } else {
                //invalid code
                response_DTO.setContent("Invalid Verification code!");
            }

        } else {
            response_DTO.setContent("Verification unavailable! Please Sign in");
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
