package com.example.criteriabuilder.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.criteriabuilder.Model.Employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Service
public class EmployeSearchDoa {
    @PersistenceContext
    private EntityManager em;

    public List<Employee> findAllBySimpleQuery(

            String firstname,
            String lastname,
            String email

    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        // ! select * from Employee

        Root<Employee> root = criteriaQuery.from(Employee.class);

        // ! prepare where clause
        // ! where firtnamePredicate "%avichal"

        Predicate firstnamePredicate = criteriaBuilder.like(root.get("firstname"), "%" + firstname + "%");
        Predicate lastnamePredicate = criteriaBuilder.like(root.get("lastname"), "%" + lastname + "%");
        Predicate emailPredicates = criteriaBuilder.like(root.get("email"), "%" + email + "%");

        Predicate firstnameorLastnamePredicates = criteriaBuilder.or(firstnamePredicate, lastnamePredicate);

        Predicate andEmailPredicates = criteriaBuilder.and(firstnameorLastnamePredicates, emailPredicates);

        criteriaQuery.where(andEmailPredicates);
        TypedQuery<Employee> query = em.createQuery(criteriaQuery);
        return query.getResultList();

    }

    public List<Employee> findAllByCriteria(
            SearchRequest request) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        List<Predicate> predicates = new ArrayList<>();

        // ! select * from employee
        Root<Employee> root = criteriaQuery.from(Employee.class);
        if (request.getFirstname() != null) {
            Predicate firstnamePredicate = criteriaBuilder.like(root.get("firstname"),
                    "%" + request.getFirstname() + "%");
            predicates.add(firstnamePredicate);
        }
        if (request.getLastname() != null) {
            Predicate lastnamePredicate = criteriaBuilder.like(root.get("lastname"),
                    "%" + request.getLastname() + "%");
            predicates.add(lastnamePredicate);
        }
        if (request.getEmail() != null) {
            Predicate emailPredicate = criteriaBuilder.like(root.get("email"),
                    "%" + request.getEmail() + "%");
            predicates.add(emailPredicate);

        }
        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        TypedQuery<Employee> query = em.createQuery(criteriaQuery);
        return query.getResultList();

    }

}
