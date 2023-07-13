package com.narbase.pss.domain.admin.staff.roles

import com.narbase.pss.common.auth.loggedin.AuthorizedClientData
import com.narbase.pss.data.access.roles.RolesDao
import com.narbase.pss.data.conversions.id.toModel
import com.narbase.pss.data.conversions.roles.privilegesEnums
import com.narbase.pss.data.conversions.roles.toDto
import com.narbase.pss.data.models.utils.ListAndTotal
import com.narbase.pss.domain.user.crud.CrudController
import com.narbase.pss.dto.models.roles.RoleDto
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


class RolesCurdController : CrudController<RoleDto, Unit>(RoleDto::class, Unit::class) {

    private val dao = RolesDao

    override fun getItemsList(
        pageNo: Long,
        pageSize: Int,
        searchTerm: String,
        filters: Map<String, String>,
        data: Unit?,
        clientData: AuthorizedClientData?
    ): ListAndTotal<RoleDto> {
        val listAndCount = transaction { dao.getList(pageNo, pageSize, searchTerm) }
        return ListAndTotal(listAndCount.list.map { it.toDto() }, listAndCount.total)
    }

    override fun createItem(item: RoleDto, clientData: AuthorizedClientData?): RoleDto {
        return transaction {
            val id = dao.create(item.name, item.privilegesEnums)
            val model = dao.get(id)
            model.toDto()
        }
    }

    override fun updateItem(item: RoleDto, clientData: AuthorizedClientData?): RoleDto {
        return transaction {
            val id = item.id!!
            dao.update(id.toModel(), item.name, item.privilegesEnums)
            val model = dao.get(id.toModel())
            model.toDto()
        }
    }

    override fun deleteItem(id: UUID?, clientData: AuthorizedClientData?) {
        transaction { dao.delete(id!!) }
    }
}