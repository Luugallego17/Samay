package com.samay.app.data.kit

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

/**
 * Repositorio en memoria para desarrollo y @Preview, sin tocar Room.
 * P4 (Karen) puede usar esto HOY para integrar Therapy con un kit mock,
 * sin esperar la persistencia real (C5).
 *
 * Arranca con el kit default "respiración + lluvia" (Plan B del README §7).
 */
class FakeKitRepository(
    seed: List<Kit> = listOf(
        Kit(id = 1, type = KitType.POEM, contentId = "salmo23", title = "Salmo 23")
    )
) : KitRepository {

    private val kits = MutableStateFlow(seed)

    val state: StateFlow<List<Kit>> = kits

    override fun observeKits() = kits
    override fun observeActiveKit() = kits.map { it.maxByOrNull(Kit::createdAt) }
    override suspend fun getById(id: Long): Kit? = kits.value.firstOrNull { it.id == id }

    override suspend fun saveKit(kit: Kit): Long {
        val newId = if (kit.id == 0L) (kits.value.maxOfOrNull { it.id } ?: 0L) + 1 else kit.id
        val saved = kit.copy(id = newId)
        kits.value = kits.value.filterNot { it.id == newId } + saved
        return newId
    }

    override suspend fun deleteKit(kit: Kit) { kits.value = kits.value.filterNot { it.id == kit.id } }
    override suspend fun clear() { kits.value = emptyList() }
}
