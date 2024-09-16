//package controller;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import dto.Response_DTO;
//import dto.User_DTO;
//import entity.Product;
//import java.io.IOException;
//import java.util.List;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import model.HibernateUtil;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Order;
//
//@WebServlet(name = "CheckSignIn", urlPatterns = {"/CheckSignIn"})
//public class CheckSignIn extends HttpServlet {
//    
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        
//        JsonObject jsonObject = new JsonObject();
//        
//        Response_DTO response_DTO = new Response_DTO();
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        
//        if (request.getSession().getAttribute("user") != null) {
//            //already signed in 
//            User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
//            response_DTO.setSuccess(true);
//            response_DTO.setContent(user_DTO);
//        }
//        
//        jsonObject.add("response_dto", gson.toJsonTree(response_DTO));
//
//        //get 3 most recent products
//        Criteria criteria = session.createCriteria(Product.class);
//        criteria.addOrder(Order.desc("id"));
//        criteria.setMaxResults(3);
//        List<Product> productList = criteria.list();
//        for (Product product : productList) {
//            product.setUser(null);
//        }
//        
//        Gson gson1 = new Gson();
//        jsonObject.add("products", gson1.toJsonTree(productList));
//        
//        response.setContentType("application/json");
//        response.getWriter().write(gson.toJson(jsonObject));
//        
//    }
//    
//}

package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

@WebServlet(name = "CheckSignIn", urlPatterns = {"/CheckSignIn"})
public class CheckSignIn extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        JsonObject jsonObject = new JsonObject();
        
        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        
        // Start Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        // Check if user is signed in
        if (request.getSession().getAttribute("user") != null) {
            // User is already signed in
            User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
            response_DTO.setSuccess(true);
            response_DTO.setContent(user_DTO);
        }
        
        // Add response DTO to JSON object
        jsonObject.add("response_dto", gson.toJsonTree(response_DTO));

        // Fetch 3 most recent products from the database
        Criteria criteria = session.createCriteria(Product.class);
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(3);
        List<Product> productList = criteria.list();

        // Detach user reference from products (if present) to avoid serialization issues
        for (Product product : productList) {
            product.setUser(null); // Assuming Product has a 'user' reference that we don't need in response
        }
        
        // Add products to JSON response
        Gson productGson = new Gson();
        jsonObject.add("products", productGson.toJsonTree(productList));
        
        // Set response content type and send JSON response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
    }
}

