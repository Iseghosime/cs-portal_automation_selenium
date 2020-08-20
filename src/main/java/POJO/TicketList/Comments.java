package POJO.TicketList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comments {
    private String agentId;

    private String interactionId;

    private TicketPool ticketPool;

    private String commentType;

    private String agentName;

    private String comment;

    private String id;

    private String updatedOn;

    private String createdOn;
}
