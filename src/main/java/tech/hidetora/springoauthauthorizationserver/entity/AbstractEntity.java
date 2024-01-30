package tech.hidetora.springoauthauthorizationserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Data
public abstract class AbstractEntity<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public abstract T getId();

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedAt;

    @Version
    private Long version;
}
