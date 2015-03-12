/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.web.controller;

import hotel.web.ejb.HotelFacade;
import hotel.web.model.Hotel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mitch
 */
public class HotelController extends HttpServlet {

    @EJB
    private HotelFacade hotelService;
     
    private static final String RESULT_PAGE = "/home.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        

        List<Hotel> allHotels = hotelService.findAll();
        request.setAttribute("filteredHotels", allHotels);
        List<Hotel> filteredHotels = new ArrayList<>();

        if (request.getParameter("hotelId") != null) {
            Hotel hotel = hotelService.find(Integer.valueOf(request.getParameter("hotelId")));
            request.setAttribute("hotelId", hotel.getHotelId());
            request.setAttribute("name", hotel.getHotelName());
            request.setAttribute("address", hotel.getStreetAddress());
            request.setAttribute("city", hotel.getCity());
            request.setAttribute("notes", hotel.getNotes());
            request.setAttribute("postalCode", hotel.getPostalCode());
            request.setAttribute("state", hotel.getState());
            request.setAttribute("tableVisible", "block");
        } else {
            request.setAttribute("tableVisible", "none");
        }
        if (request.getParameter("submit") != null && request.getParameter("hotelId") != null) {
            switch (request.getParameter("submit")) {
                case "Save":
                    Hotel hotel = hotelService.find(Integer.valueOf(request.getParameter("hotelId")));
                    hotel.setHotelName(request.getParameter("name"));
                    hotel.setStreetAddress(request.getParameter("address"));
                    hotel.setCity(request.getParameter("city"));
                    hotel.setState(request.getParameter("state"));
                    hotel.setPostalCode(request.getParameter("postalCode"));
                    hotel.setNotes(request.getParameter("notes"));
                    hotelService.edit(hotel);
                    break;
                case "Delete":
                    hotelService.remove(hotelService.find(Integer.valueOf(request.getParameter("hotelId"))));
                    break;
            }
            request.setAttribute("tableVisible", "none");
        }
        if (request.getParameter("submit") != null && request.getParameter("submit").equals("Add Hotel")) {
            Hotel hotel = new Hotel();
            hotel.setHotelName(request.getParameter("name"));
            hotel.setStreetAddress(request.getParameter("address"));
            hotel.setCity(request.getParameter("city"));
            hotel.setState(request.getParameter("state"));
            hotel.setPostalCode(request.getParameter("postalCode"));
            hotel.setNotes(request.getParameter("notes"));
            hotelService.create(hotel);
            request.setAttribute("tableVisible", "none");
        }
        if (request.getParameter("submit") != null && request.getParameter("submit").equals("Filter")) {
            
            allHotels = hotelService.findAll();
            if (request.getParameter("filterState") != null && !(request.getParameter("filterState").isEmpty())) {
                if (request.getParameter("filterCity") != null && !(request.getParameter("filterCity").isEmpty())) {
                    for (Hotel hotel : allHotels) {
                        if (hotel.getCity().toLowerCase().equals(request.getParameter("filterCity").toLowerCase())
                                && hotel.getState().toLowerCase().equals(request.getParameter("filterState").toLowerCase())) {
                            filteredHotels.add(hotel);
                        }
                    }
                    request.setAttribute("filteredHotels", filteredHotels);
                } else {
                    for (Hotel hotel : allHotels) {
                        if (hotel.getState().toLowerCase().equals(request.getParameter("filterState").toLowerCase())) {
                            filteredHotels.add(hotel);
                        }
                    }
                    request.setAttribute("filteredHotels", filteredHotels);
                }
            } else if(request.getParameter("filterPost") != null && !(request.getParameter("filterPost").isEmpty())){
                for (Hotel hotel : allHotels) {
                        if (hotel.getPostalCode().toLowerCase().equals(request.getParameter("filterPost").toLowerCase())) {
                            filteredHotels.add(hotel);
                        }
                    }
                request.setAttribute("filteredHotels", filteredHotels);
            } else if(request.getParameter("filterName") != null && !(request.getParameter("filterName").isEmpty())){
                for (Hotel hotel : allHotels) {
                        if (hotel.getHotelName().toLowerCase().equals(request.getParameter("filterName").toLowerCase())) {
                            filteredHotels.add(hotel);
                        }
                    }
                request.setAttribute("filteredHotels", filteredHotels);
            } else if(request.getParameter("filterCity") != null && !(request.getParameter("filterCity").isEmpty())){
                for (Hotel hotel : allHotels) {
                        if (hotel.getCity().toLowerCase().equals(request.getParameter("filterCity").toLowerCase())) {
                            filteredHotels.add(hotel);
                        }
                    }
                request.setAttribute("filteredHotels", filteredHotels);
            } else {
            request.setAttribute("filteredHotels", hotelService.findAll());
            }
            request.setAttribute("tableVisible", "none");
            
        }
        List<String> states = getAllStates(allHotels);
        request.setAttribute("states", states);

        RequestDispatcher view
                = request.getRequestDispatcher(RESULT_PAGE);
        view.forward(request, response);
    }

    private List<String> getAllStates(List<Hotel> hotels) {
        HashSet<String> allStates = new HashSet<>();
        for (Hotel hotel : hotels) {
            allStates.add(hotel.getState());
        }
        List<String> states = new ArrayList<>(allStates);
        Collections.sort(states);
        return states;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
