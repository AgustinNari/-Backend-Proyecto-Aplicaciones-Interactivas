package com.uade.tpo.marketplace.repository;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.Review;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.request.SellerFilter;
import com.uade.tpo.marketplace.entity.dto.response.SellerListDTO;
import com.uade.tpo.marketplace.entity.enums.KeyStatus;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepositoryCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Repository
public class UserRepositoryCustomImpl implements IUserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Page<SellerListDTO> findSellersWithAggregates(SellerFilter filter, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<User> u = cq.from(User.class);



        Subquery<Double> avgRatingSq = cq.subquery(Double.class);
        {
            Root<Review> r = avgRatingSq.from(Review.class);
            avgRatingSq.select(cb.avg(r.get("rating").as(Double.class)));
            avgRatingSq.where(cb.equal(r.get("seller"), u), cb.isTrue(r.get("visible")));
        }


        Subquery<Long> ratingCountSq = cq.subquery(Long.class);
        {
            Root<Review> r = ratingCountSq.from(Review.class);
            ratingCountSq.select(cb.count(r.get("id")));
            ratingCountSq.where(cb.equal(r.get("seller"), u), cb.isTrue(r.get("visible")));
        }


        Subquery<Long> soldKeysSq = cq.subquery(Long.class);
        {
            Root<DigitalKey> dk = soldKeysSq.from(DigitalKey.class);
            Join<DigitalKey, Product> p = dk.join("product", JoinType.INNER);
            soldKeysSq.select(cb.coalesce(cb.count(dk.get("id")), 0L));
            soldKeysSq.where(cb.equal(p.get("seller"), u), cb.equal(dk.get("status"), KeyStatus.SOLD));
        }


        Subquery<Long> amountSoldSq = cq.subquery(Long.class);
        {
            Root<OrderItem> oi = amountSoldSq.from(OrderItem.class);
            Join<OrderItem, Product> p2 = oi.join("product", JoinType.INNER);
            amountSoldSq.select(cb.coalesce(cb.sum(oi.get("quantity").as(Long.class)), 0L));
            amountSoldSq.where(cb.equal(p2.get("seller"), u));
        }


        cq.multiselect(
                u.get("id").alias("id"),
                u.get("displayName").alias("displayName"),
                u.get("avatar").alias("avatar"),
                u.get("avatarContentType").alias("avatarContentType"),
                avgRatingSq.alias("avgRating"),
                ratingCountSq.alias("ratingCount"),
                soldKeysSq.alias("soldKeys"),
                amountSoldSq.alias("amountSold")
        );


        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(u.get("role"), Role.SELLER));

        predicates.add(cb.isTrue(u.get("active")));


        if (filter != null) {
            if (filter.getSellerIds() != null && !filter.getSellerIds().isEmpty()) {
                predicates.add(u.get("id").in(filter.getSellerIds()));
            }
            if (filter.getMinAvgRating() != null) {

                Subquery<Long> avgFilter = cq.subquery(Long.class);
                Root<Review> r2 = avgFilter.from(Review.class);
                Expression<Long> sellerIdExpr = r2.get("seller").get("id");
                avgFilter.select(sellerIdExpr);
                avgFilter.where(cb.equal(r2.get("seller"), u), cb.isTrue(r2.get("visible")));
                avgFilter.groupBy(sellerIdExpr);
                avgFilter.having(cb.greaterThanOrEqualTo(cb.avg(r2.get("rating")), filter.getMinAvgRating()));
                predicates.add(cb.exists(avgFilter));
            }

            if (filter.getMinAmountSold() != null) {
                Subquery<Long> amtFilter = cq.subquery(Long.class);
                Root<OrderItem> oi2 = amtFilter.from(OrderItem.class);
                Join<OrderItem, Product> p3 = oi2.join("product", JoinType.INNER);
                Expression<Long> sellerIdExpr2 = p3.get("seller").get("id");
                amtFilter.select(sellerIdExpr2);
                amtFilter.where(cb.equal(p3.get("seller"), u));
                amtFilter.groupBy(sellerIdExpr2);
                amtFilter.having(cb.greaterThanOrEqualTo(cb.coalesce(cb.sum(oi2.get("quantity").as(Long.class)), 0L), filter.getMinAmountSold()));
                predicates.add(cb.exists(amtFilter));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));


        List<Order> orders = new ArrayList<>();
        if (pageable.getSort().isSorted()) {
            for (Sort.Order so : pageable.getSort()) {
                String prop = so.getProperty();
                boolean asc = so.isAscending();
                switch (prop) {
                    case "avgRating":
                        orders.add(asc ? cb.asc(avgRatingSq) : cb.desc(avgRatingSq));
                        break;
                    case "ratingCount":
                        orders.add(asc ? cb.asc(ratingCountSq) : cb.desc(ratingCountSq));
                        break;
                    case "soldKeys":
                        orders.add(asc ? cb.asc(soldKeysSq) : cb.desc(soldKeysSq));
                        break;
                    case "amountSold":
                        orders.add(asc ? cb.asc(amountSoldSq) : cb.desc(amountSoldSq));
                        break;
                    case "displayName":
                        orders.add(asc ? cb.asc(u.get("displayName")) : cb.desc(u.get("displayName")));
                        break;
                    case "createdAt":
                        orders.add(asc ? cb.asc(u.get("createdAt")) : cb.desc(u.get("createdAt")));
                        break;
                    default:

                        orders.add(asc ? cb.asc(u.get(prop)) : cb.desc(u.get(prop)));
                }
            }
        } else {
            orders.add(cb.desc(u.get("createdAt")));
        }
        cq.orderBy(orders);


        List<Tuple> tuples = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        List<SellerListDTO> dtos = tuples.stream().map(t -> {
            SellerListDTO dto = new SellerListDTO();
            dto.setId((Long) t.get("id"));
            dto.setDisplayName((String) t.get("displayName"));


            Blob avatarBlob = (Blob) t.get("avatar");
            String avatarContentType = (String) t.get("avatarContentType");
            dto.setAvatarContentType(avatarContentType);
            if (avatarBlob != null) {
                try {
                    String base64 = blobToBase64(avatarBlob);
                    if (base64 != null && avatarContentType != null && !avatarContentType.isBlank()) {
                        dto.setAvatarDataUrl("data:" + avatarContentType + ";base64," + base64);
                    } else {
                        dto.setAvatarDataUrl(null);
                    }
                } catch (RuntimeException ex) {
                    dto.setAvatarDataUrl(null);
                }
            }

            dto.setAvgRating(t.get("avgRating") != null ? ((Number) t.get("avgRating")).doubleValue() : null);
            dto.setRatingCount(t.get("ratingCount") != null ? ((Number) t.get("ratingCount")).longValue() : 0L);
            dto.setSoldKeys(t.get("soldKeys") != null ? ((Number) t.get("soldKeys")).longValue() : 0L);
            dto.setAmountSold(t.get("amountSold") != null ? ((Number) t.get("amountSold")).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());

        long total = computeCount(filter);

        return new PageImpl<>(dtos, pageable, total);
    }


    private long computeCount(SellerFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<User> u = cq.from(User.class);
        cq.select(cb.countDistinct(u.get("id")));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(u.get("role"), Role.SELLER));
        predicates.add(cb.isTrue(u.get("active")));

        if (filter != null) {
            if (filter.getSellerIds() != null && !filter.getSellerIds().isEmpty()) {
                predicates.add(u.get("id").in(filter.getSellerIds()));
            }
            if (filter.getMinAvgRating() != null) {
                Subquery<Long> avgFilter = cq.subquery(Long.class);
                Root<Review> r2 = avgFilter.from(Review.class);
                Expression<Long> sid = r2.get("seller").get("id");
                avgFilter.select(sid);
                avgFilter.where(cb.equal(r2.get("seller"), u), cb.isTrue(r2.get("visible")));
                avgFilter.groupBy(sid);
                avgFilter.having(cb.greaterThanOrEqualTo(cb.avg(r2.get("rating")), filter.getMinAvgRating()));
                predicates.add(cb.exists(avgFilter));
            }
            if (filter.getMinAmountSold() != null) {
                Subquery<Long> amtFilter = cq.subquery(Long.class);
                Root<OrderItem> oi2 = amtFilter.from(OrderItem.class);
                Join<OrderItem, Product> pj = oi2.join("product", JoinType.INNER);
                Expression<Long> sid2 = pj.get("seller").get("id");
                amtFilter.select(sid2);
                amtFilter.where(cb.equal(pj.get("seller"), u));
                amtFilter.groupBy(sid2);
                amtFilter.having(cb.greaterThanOrEqualTo(cb.coalesce(cb.sum(oi2.get("quantity").as(Long.class)), 0L), filter.getMinAmountSold()));
                predicates.add(cb.exists(amtFilter));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        Long result = em.createQuery(cq).getSingleResult();
        return result != null ? result : 0L;
    }


    private String blobToBase64(Blob blob) {
        try {
            long length = blob.length();
            if (length == 0) return null;
            if (length > Integer.MAX_VALUE) throw new RuntimeException("Blob demasiado grande");
            byte[] bytes = blob.getBytes(1, (int) length);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (SQLException e) {
            throw new RuntimeException("Error leyendo el avatar blob", e);
        }
    }
}
