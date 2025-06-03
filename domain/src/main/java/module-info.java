module org.example.domain {
    requires org.hibernate.orm.core;
    requires org.example.server.api;
    requires static jakarta.persistence;
    requires static jakarta.inject;
    requires static jakarta.cdi;

    exports entity;
    exports service_impl;
    exports workWithHibernate;
    
    opens entity to org.hibernate.orm.core;
} 