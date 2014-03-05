/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.operaciones;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.daw.bean.TareaBean;
import net.daw.bean.UsuarioBean;
import net.daw.dao.TareaDao;
import net.daw.helper.Conexion;
import net.daw.helper.FilterBean;

/**
 *
 * @author al037684
 */
public class TareaGetpage implements GenericOperation {
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        UsuarioBean oUsuarioBean = (UsuarioBean) request.getSession().getAttribute("usuarioBean");
        String idUserSesion = oUsuarioBean.getId().toString();
        
        
        request.setAttribute("systemfilter","id_usuario");
        request.setAttribute("systemfilteroperator","equals");
        request.setAttribute("systemfiltervalue",idUserSesion);
        
        String data;
        try {
            int rpp;
            if (request.getParameter("rpp") == null) {
                rpp = 10;
            } else {
                rpp = Integer.parseInt(request.getParameter("rpp"));
            }
            int page;
            if (request.getParameter("page") == null) {
                page = 1;
            } else {
                page = Integer.parseInt(request.getParameter("page"));
            }

            ArrayList<FilterBean> alFilter = new ArrayList<>();
            if (request.getParameter("filter") != null) {
                if (request.getParameter("filteroperator") != null) {
                    if (request.getParameter("filtervalue") != null) {
                        FilterBean oFilterBean = new FilterBean();
                        oFilterBean.setFilter(request.getParameter("filter"));
                        oFilterBean.setFilterOperator(request.getParameter("filteroperator"));
                        oFilterBean.setFilterValue(request.getParameter("filtervalue"));
                        oFilterBean.setFilterOrigin("user");
                        alFilter.add(oFilterBean);
                    }
                }
            }
            /*if (request.getParameter("systemfilter") != null) {
                if (request.getParameter("systemfilteroperator") != null) {
                    if (request.getParameter("systemfiltervalue") != null) {
                        FilterBean oFilterBean = new FilterBean();
                        oFilterBean.setFilter(request.getParameter("systemfilter"));
                        oFilterBean.setFilterOperator(request.getParameter("systemfilteroperator"));
                        oFilterBean.setFilterValue(request.getParameter("systemfiltervalue"));
                        oFilterBean.setFilterOrigin("system");
                        alFilter.add(oFilterBean);
                    }
                }
            }*/
            if (request.getAttribute("systemfilter") != null) {
                if (request.getAttribute("systemfilteroperator") != null) {
                    if (request.getAttribute("systemfiltervalue") != null) {
                        FilterBean oFilterBean = new FilterBean();
                        oFilterBean.setFilter((String) request.getAttribute("systemfilter"));
                        oFilterBean.setFilterOperator((String) request.getAttribute("systemfilteroperator"));
                        oFilterBean.setFilterValue((String) request.getAttribute("systemfiltervalue"));
                        oFilterBean.setFilterOrigin("system");
                        alFilter.add(oFilterBean);
                    }
                }
            }
            if (alFilter.isEmpty()) {
                alFilter = null;
            }
            HashMap<String, String> hmOrder = new HashMap<>();

            if (request.getParameter("order") != null) {
                if (request.getParameter("ordervalue") != null) {
                    hmOrder.put(request.getParameter("order"), request.getParameter("ordervalue"));
                } else {
                    hmOrder = null;
                }
            } else {
                hmOrder = null;
            }
            TareaDao oTareaDAO = new TareaDao(Conexion.getConection());
            List<TareaBean> oTareas = oTareaDAO.getPage(rpp, page, alFilter, hmOrder);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy");
            Gson gson = gsonBuilder.create();
            data = gson.toJson(oTareas);
            data = "{\"list\":" + data + "}";
            return data;
        } catch (Exception e) {
            throw new ServletException("TareaGetJson: View Error: " + e.getMessage());
        }
    }
    
}
