//package controller;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import dto.Cart_DTO;
//import dto.Response_DTO;
//import dto.User_DTO;
//import entity.Cart;
//import entity.User;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import model.HibernateUtil;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Restrictions;
//
//@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
//public class SignIn extends HttpServlet {
//    
//    @Override
//    protected void doPost(HttpServletRequest requset, HttpServletResponse response) throws ServletException, IOException {
//        
//        Response_DTO response_DTO = new Response_DTO();
//        
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        User_DTO user_DTO = gson.fromJson(requset.getReader(), User_DTO.class);
//        
//        if (user_DTO.getEmail().isEmpty()) {
//            response_DTO.setContent("Please enter your Email");
//            
//        } else if (user_DTO.getPassword().isEmpty()) {
//            response_DTO.setContent("Please enter your Password");
//            
//        } else {
//            
//            Session session = HibernateUtil.getSessionFactory().openSession();
//            
//            Criteria criteria = session.createCriteria(User.class);
//            criteria.add(Restrictions.eq("email", user_DTO.getEmail()));
//            criteria.add(Restrictions.eq("password", user_DTO.getPassword()));
//            
//            if (!criteria.list().isEmpty()) {
//                
//                User user = (User) criteria.uniqueResult();
//                
//                if (!user.getVerification().equals("Verified")) {
//                    //not verified
//
//                    requset.getSession().setAttribute("email", user_DTO.getEmail());
//                    response_DTO.setContent("Unverified");
//                    
//                } else {
//                    //verified
//
//                    user_DTO.setFirst_name(user.getFirst_name());
//                    user_DTO.setLast_name(user.getLast_name());
//                    user_DTO.setPassword(null);
//                    requset.getSession().setAttribute("user", user_DTO);
//
//                    //Transfer session cart to DB cart
//                    if (requset.getSession().getAttribute("sessionCart") != null) {
//                        
//                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) requset.getSession().getAttribute("sessionCart");
//                        
//                        Criteria criteria2 = session.createCriteria(Cart.class);
//                        criteria2.add(Restrictions.eq("user", user));
//                        List<Cart> dbCart = criteria2.list();
//                        
//                        if (dbCart.isEmpty()) {
//                            //DB cart is empty
//                            //Add all session cart items into DB cart
//
//                            for (Cart_DTO cart_DTO : sessionCart) {
//                                
//                                Cart cart = new Cart();
//                                cart.setProduct(cart_DTO.getProduct()); //* user is null
//                                cart.setQty(cart_DTO.getQty());
//                                cart.setUser(user);
//                                session.save(cart);
//                            }
//                            
//                        } else {
//                            //found items in DB cart
//
//                            for (Cart_DTO cart_DTO : sessionCart) {
//                                
//                                boolean isFoundInDBCart = false;
//                                for (Cart cart : dbCart) {
//                                    
//                                    if (cart_DTO.getProduct().getId() == cart.getProduct().getId()) {
//                                        //same item found in session cart & DB cart
//                                        isFoundInDBCart = true;
//                                        
//                                        if ((cart_DTO.getQty() + cart.getQty()) <= cart.getProduct().getQty()) {
//                                            //quantity available
//                                            cart.setQty(cart_DTO.getQty() + cart.getQty());
//                                            session.update(cart);
//                                            
//                                        } else {
//                                            //quantity not available
//                                            //set max available qty (not required)
//                                            cart.setQty(cart.getProduct().getQty());
//                                            session.update(cart);
//                                        }
//                                    }
//                                    
//                                }
//                                
//                                if (!isFoundInDBCart) {
//                                    //not found in DB cart
//                                    Cart cart = new Cart();
//                                    cart.setProduct(cart_DTO.getProduct()); //* user is null
//                                    cart.setQty(cart_DTO.getQty());
//                                    cart.setUser(user);
//                                    session.save(cart);
//                                }
//                                
//                            }
//                            
//                        }
//                        
//                        requset.getSession().removeAttribute("sessionCart");
//                        session.beginTransaction().commit();
//                        
//                    }
//                    
//                    response_DTO.setSuccess(true);
//                    response_DTO.setContent("Sign in success");
//                }
//                
//            } else {
//                response_DTO.setContent("Invalid Details!");
//            }
//        }
//        
//        response.setContentType("application/json");
//        response.getWriter().write(gson.toJson(response_DTO));
//        
//    }
//    
//}
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_DTO = gson.fromJson(request.getReader(), User_DTO.class);

        if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please enter your Email");
        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please enter your Password");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", user_DTO.getEmail()));
            criteria.add(Restrictions.eq("password", user_DTO.getPassword()));

            if (!criteria.list().isEmpty()) {
                User user = (User) criteria.uniqueResult();

                if (!user.getVerification().equals("Verified")) {
                    // User is not verified
                    request.getSession().setAttribute("email", user_DTO.getEmail());
                    response_DTO.setContent("Unverified");
                } else {
                    // User is verified
                    user_DTO.setFirst_name(user.getFirst_name());
                    user_DTO.setLast_name(user.getLast_name());
                    user_DTO.setPassword(null); // Do not send password in the response
                    request.getSession().setAttribute("user", user_DTO);

                    // Transfer session cart to DB cart
                    if (request.getSession().getAttribute("sessionCart") != null) {
                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) request.getSession().getAttribute("sessionCart");

                        // Fetch DB cart for this user
                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        List<Cart> dbCart = criteria2.list();

                        for (Cart_DTO cart_DTO : sessionCart) {
                            boolean foundInDb = false;

                            // Check if the product in sessionCart exists in the dbCart
                            for (Cart dbCartItem : dbCart) {
                                if (dbCartItem.getProduct().getId() == cart_DTO.getProduct().getId()) {
                                    foundInDb = true;

                                    // Update qty if product is already in the DB cart
//                                    int newQty = Math.min(dbCartItem.getQty() + cart_DTO.getQty(), dbCartItem.getProduct().getQty());
                                    int newQty = Math.min(dbCartItem.getQty() + cart_DTO.getQty(), dbCartItem.getProduct().getQty());

                                    dbCartItem.setQty(newQty);
                                    session.update(dbCartItem);
                                }
                            }

                            // If product not found in DB cart, create a new Cart entry
                            if (!foundInDb) {
                                Cart newCartItem = new Cart();
                                Product product = (Product) session.get(Product.class, cart_DTO.getProduct().getId());
                                newCartItem.setProduct(product);
                                newCartItem.setQty(cart_DTO.getQty());
                                newCartItem.setUser(user);
                                session.save(newCartItem);
                            }
                        }

                        // Remove session cart after transferring to DB
                        request.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();
                    }

                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Sign in success");
                }
            } else {
                response_DTO.setContent("Invalid Details!");
            }

            session.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }
}
