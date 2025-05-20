package mx.dgtic.unam.simin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "instrumentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instrumento")
    private Integer idInstrumento;

    @NotBlank(message = "El nombre del instrumento es obligatorio.")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres.")
    @Column(name = "nombre", length = 100, unique = true, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_tipo_instrumento", nullable = false)
    private TipoInstrumento tipoInstrumento;

    @Min(value = 0, message = "El plazo en meses no puede ser negativo.")
    @Max(value = 12, message = "El plazo en meses no puede exceder 12.")
    @Column(name = "plazo_meses")
    private Integer plazoMeses;

    @Min(value = 0, message = "El plazo en días no puede ser negativo.")
    @Column(name = "dias_plazo")
    private Integer diasPlazo;

    @Min(value = 0, message = "El plazo en años no puede ser negativo.")
    @Column(name = "plazo_anos")
    private Integer plazoAnos;

    @DecimalMin(value = "0.01", inclusive = true, message = "La tasa de interés debe ser mayor a 0.")
    @Column(name = "tasa_interes", precision = 5, scale = 2, nullable = false)
    private BigDecimal tasaInteres;

    @DecimalMin(value = "0.01", inclusive = true, message = "El valor nominal debe ser mayor a 0.")
    @Column(name = "valor_nominal", precision = 15, scale = 4, nullable = false)
    private BigDecimal valorNominal;

    @DecimalMin(value = "0.00", inclusive = true, message = "El precio de mercado no puede ser negativo.")
    @Column(name = "precio_mercado", precision = 15, scale = 4)
    private BigDecimal precioMercado = BigDecimal.ZERO;

    @NotNull(message = "La frecuencia de pagos es obligatoria.")
    @Column(name = "frecuencia_pagos", nullable = false)
    @Enumerated(EnumType.STRING)
    private FrecuenciaPagos frecuenciaPagos;

    @NotNull(message = "El tipo de rendimiento es obligatorio.")
    @Column(name = "tipo_rendimiento", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoRendimiento tipoRendimiento;

    @Size(max = 250, message = "La descripción adicional no debe exceder los 250 caracteres.")
    @Column(name = "descripcion_adicional", length = 250)
    private String descripcionAdicional;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDateTime fechaVencimiento;

    @Column(name = "tasa_cupon", precision = 10, scale = 4)
    private BigDecimal tasaCupon;

    public enum FrecuenciaPagos {
        Mensual, Trimestral, Semestral, Anual, Diario
    }

    public enum TipoRendimiento {
        Fijo, Variable
    }
}