package ifpb.pdm.questao_05_webserver.service;

import ifpb.pdm.questao_05_webserver.model.MyChatMessage;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author natarajan
 */
@DataSourceDefinition(
        name = "java:app/jdbc/message",
        className = "org.postgresql.Driver",
        url = "jdbc:postgresql://docker-postgres:5432/messagedb",        
//        url = "jdbc:postgresql://localhost:5432/messagedb",
        user = "postgres",
        password = "12345"
)

@Stateless
public class MessageService {

    @PersistenceContext
    private EntityManager em;

    public MyChatMessage salvar(MyChatMessage message) {
        em.persist(message);
        return message;

    }

    public MyChatMessage buscar(Long key) {
        return em.find(MyChatMessage.class, key);
    }

    
    public List<MyChatMessage> listarDepoisDoId(Long id) {
                
        TypedQuery<MyChatMessage> query = 
                em.createQuery("SELECT m FROM MyChatMessage m WHERE m.id > :id", MyChatMessage.class)
                .setParameter("id", id);
        return query.getResultList();
    }
}
