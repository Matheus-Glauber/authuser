package com.ead.authuser.models;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_USERS")
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel implements Serializable {
    private static final long serialVersionUID = 5086062526508173869L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50, name = "USER_NAME")
    private String userName;

    @Column(nullable = false, unique = true, length = 50, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "PASSWORD")
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 150, name = "FULL_NAME")
    private String fullname;

    @Column(nullable = false, name = "USER_STATUS")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false, name = "USER_TYPE")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(length = 15, name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(length = 11, name = "CPF")
    private String cpf;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(nullable = false, name = "CREATION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;

    @Column(name = "LAST_UPDATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastUpdateDate;
}
