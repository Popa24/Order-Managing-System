package org.example.order.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.client.model.ClientEntity;
import org.example.product.model.ProductEntity;

import java.util.Map;
import java.util.Set;


@Builder
@AllArgsConstructor
@Entity
@Table(name = "order")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    // Many-to-One relationship with ClientEntity
    @ManyToOne
    @JoinColumn(name = "client_id")
    ClientEntity client;

    // Many-to-Many relationship with ProductEntity
    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @ToString.Exclude
    Set<ProductEntity> products;

    // Additional field for storing quantities of products
    @ElementCollection
    @CollectionTable(name = "order_product_quantity", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "opq_quantity")
    @MapKeyJoinColumn(referencedColumnName = "product_id")

    Map<ProductEntity, Integer> productQuantities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

