package com.fraternity.fsp.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A HelpAction.
 */
@Entity
@Table(name = "help_action")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HelpAction implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JsonIgnoreProperties("helpActions")
    private HelpOffer helpOffer;

    @ManyToOne
    @JsonIgnoreProperties("helpActions")
    private HelpRequest helpRequest;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public HelpAction message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HelpOffer getHelpOffer() {
        return helpOffer;
    }

    public HelpAction helpOffer(HelpOffer helpOffer) {
        this.helpOffer = helpOffer;
        return this;
    }

    public void setHelpOffer(HelpOffer helpOffer) {
        this.helpOffer = helpOffer;
    }

    public HelpRequest getHelpRequest() {
        return helpRequest;
    }

    public HelpAction helpRequest(HelpRequest helpRequest) {
        this.helpRequest = helpRequest;
        return this;
    }

    public void setHelpRequest(HelpRequest helpRequest) {
        this.helpRequest = helpRequest;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HelpAction helpAction = (HelpAction) o;
        if (helpAction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), helpAction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HelpAction{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
