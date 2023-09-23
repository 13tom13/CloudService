package ru.netology.cloudservicediplom.model;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cloud_user")
public class CloudUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String login;
    private String password;
}
