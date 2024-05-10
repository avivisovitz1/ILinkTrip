package models

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class Model private constructor() {
    companion object {
        private var _instance: Model = Model()

        var data: MutableList<Trip> = mutableListOf(
            Trip(
                "Aviv Isovitz",
                "Argentina",
                "Buenos Aires",
                LocalDate.parse("2024-05-11"),
                2,
                false
            ),
            Trip(
                "Shumiel Dovianov",
                "Argentina",
                "Buenos Aires",
                LocalDate.parse("2024-05-12"),
                1,
                false
            ),
            Trip(
                "Daphne Harari",
                "Argentina",
                "Buenos Aires",
                LocalDate.parse("2024-05-11"),
                3,
                false
            ),
            Trip(
                "Einav Eliyahu",
                "Argentina",
                "Buenos Aires",
                LocalDate.parse("2024-05-12"),
                1,
                false
            ),
            Trip(
                "Michal Isovitz",
                "Argentina",
                "Buenos Aires",
                LocalDate.parse("2024-05-11"),
                4,
                false
            ),
            Trip(
                "Adir Gigi",
                "Argentina",
                "Buenos Aires",
                LocalDate.parse("2024-05-12"),
                1,
                false
            ),
            Trip(
                "Shahar Elwaya",
                "Argentina",
                "Buenos Aires",
                LocalDate.parse("2024-05-11"),
                2,
                false
            ),
            Trip(
                "Idan Svirsky",
                "Argentina",
                "Buenos Aires",
                LocalDate.parse("2024-05-12"),
                1,
                false
            )
        )

        fun instance(): Model {
            return _instance
        }
    }

    fun getAllTrips(): MutableList<Trip> {
        return data
    }

    fun addTrip(trip: Trip) {
        data = (data + trip).toMutableList()
    }
}