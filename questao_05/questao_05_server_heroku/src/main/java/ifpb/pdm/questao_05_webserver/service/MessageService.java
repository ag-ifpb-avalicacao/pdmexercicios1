package ifpb.pdm.questao_05_webserver.service;

import ifpb.pdm.questao_05_webserver.model.MyChatMessage;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author natarajan
 */

@Stateless
public class MessageService {

    @PersistenceContext (unitName = "clientePU")
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
