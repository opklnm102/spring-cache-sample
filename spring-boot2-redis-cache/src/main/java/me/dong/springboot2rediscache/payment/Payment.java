package me.dong.springboot2rediscache.payment;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by ethan.kim on 2018. 5. 19..
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Payment implements Serializable {

    private long id;

    private double amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
