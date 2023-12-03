package kr.galaxyhub.sc.common.domain

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import java.util.Objects
import java.util.UUID
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable

@MappedSuperclass
abstract class PrimaryKeyEntity : Persistable<UUID> {

    @Id
    @Column(columnDefinition = "uuid")
    private val id: UUID = UlidCreator.getMonotonicUlid().toUuid()

    @Transient
    private var _isNew = true

    override fun getId(): UUID = id

    override fun isNew(): Boolean = _isNew

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other !is HibernateProxy && this::class != other::class) {
            return false
        }
        return id == getIdentifier(other)
    }

    private fun getIdentifier(obj: Any): Any {
        return when (obj) {
            is HibernateProxy -> obj.hibernateLazyInitializer.identifier
            else -> (obj as PrimaryKeyEntity).id
        }
    }

    override fun hashCode(): Int = Objects.hashCode(id)

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }
}
