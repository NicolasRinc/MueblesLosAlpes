/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author User
 */
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioSeguridadMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.persistencia.mock.ServicioPersistenciaMock;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioPersistenciaMockLocal;

@Path("/Seguridad")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeguridadService {

    @EJB
    private IServicioSeguridadMockLocal servicioSeguridad;

    @EJB
    private IServicioPersistenciaMockLocal persistencia;

    @GET
    @Path("/ingresar")
    public Response ingresarGET(@QueryParam("nombre") String nombre, @QueryParam("contraseña") String contraseña) {
        try {
            Usuario usuario = servicioSeguridad.ingresar(nombre, contraseña);
            return Response.ok(usuario).build();
        } catch (AutenticacionException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

     @POST
    @Path("/ingresar")
    public Response ingresarPOST(Usuario usuario) {
        try {
            Usuario usuarioAutenticado = servicioSeguridad.ingresar(usuario.getLogin(), usuario.getContraseña());
            return Response.ok(usuarioAutenticado).build();
        } catch (AutenticacionException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/actualizar/{nombre}")
    public Response actualizarContraseña(@PathParam("nombre") String nombre, Usuario usuario) {
        try {
            Usuario usuarioActualizado = servicioSeguridad.ingresar(nombre, usuario.getContraseña());
            persistencia.update(usuario);
            return Response.ok(usuarioActualizado).build();
        } catch (AutenticacionException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/eliminar/{nombre}")
    public Response eliminarUsuario(@PathParam("nombre") String nombre) throws OperacionInvalidaException {
        Usuario usuario = (Usuario) persistencia.findById(Usuario.class, nombre);
        if (usuario != null) {
            persistencia.delete(usuario);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
        }
    }
}
