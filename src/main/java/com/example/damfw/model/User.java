package com.example.damfw.model;

import com.example.damfw.annotations.Column;
import com.example.damfw.annotations.Id;
import com.example.damfw.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(table = "user")
public class User {

    @Id(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}