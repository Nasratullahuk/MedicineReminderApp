package e4294395nasratullahuk.medicinereminder.database

import kotlinx.coroutines.flow.Flow

// --- 3. Repository ---
class MedicineRepository(
    private val medicineDao: MedicineDao,
    private val medicineHistoryDao: MedicineHistoryDao
) {
    val allMedicines: Flow<List<Medicine>> = medicineDao.getAllMedicines()

    suspend fun insert(medicine: Medicine): Long {
        return medicineDao.insertMedicine(medicine)
    }

    suspend fun update(medicine: Medicine) {
        medicineDao.updateMedicine(medicine)
    }

    suspend fun delete(medicine: Medicine) {
        medicineDao.deleteMedicine(medicine)
    }

    fun getMedicineById(medicineId: Int): Flow<Medicine?> {
        return medicineDao.getMedicineById(medicineId)
    }

    suspend fun insertMedicineHistory(history: MedicineHistory) {
        medicineHistoryDao.insertMedicineHistory(history)
    }

    fun getMedicineHistoryForMedicine(medicineId: Int): Flow<List<MedicineHistory>> {
        return medicineHistoryDao.getMedicineHistoryForMedicine(medicineId)
    }

    // New: Delegate to DAO to delete history for a medicine
    suspend fun deleteHistoryForMedicine(medicineId: Int) {
        medicineHistoryDao.deleteHistoryForMedicine(medicineId)
    }
}