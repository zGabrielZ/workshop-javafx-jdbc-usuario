package br.com.gabrielferreira.modelo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Telefone implements Serializable {

    @Serial
    private static final long serialVersionUID = -8629710608419903368L;

    @EqualsAndHashCode.Include
    private Long id;

    private String ddd;

    private String numero;
}
