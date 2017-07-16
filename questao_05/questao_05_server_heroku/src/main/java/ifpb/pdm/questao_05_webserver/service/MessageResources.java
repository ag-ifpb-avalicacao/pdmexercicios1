package ifpb.pdm.questao_05_webserver.service;

import ifpb.pdm.questao_05_webserver.model.MyChatMessage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author natarajan
 */
@Path("message")
@Stateless
public class MessageResources {

    @EJB
    private MessageService messageService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMessage(MyChatMessage message, 
            @Context UriInfo uriInfo) throws URISyntaxException {

        MyChatMessage saved = messageService.salvar(message);

        return Response
                .created(new URI("/message-server/api/message/" + message.getId()))
                .entity(message)
                .build();

    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(@PathParam("id") Long id) {
        MyChatMessage message  = messageService.buscar(id);
        if (message == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        return Response.ok().entity(message).build();
    }
    
    @GET
    @Path("/greater/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGreaterThan(@PathParam("id") Long id) {
        List<MyChatMessage> messages = messageService.listarDepoisDoId(id);
        
        GenericEntity<List<MyChatMessage>> entityResponse = new GenericEntity<List<MyChatMessage>>(messages) {
        };
        return Response.ok().entity(entityResponse).build();
    }
    
    
    

}
