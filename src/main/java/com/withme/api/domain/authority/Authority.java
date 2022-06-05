package com.withme.api.domain.authority;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "AUTHORITY")
@Entity
public class Authority {

   @Id
   @Column(length = 50)
   private String authorityName;

   @Builder
   public Authority(String authorityName) {
      this.authorityName = authorityName;
   }
}