package models

import org.threeten.bp.LocalDate
import java.util.Locale

class Model private constructor() {
    companion object {
        private var _instance: Model = Model()

        val user: User = User(
            "323100347", "avivisovitz@gmail.com", "Aviv", "Isovitz",
            22, "male", "0528293085", "Aviv1234"
        )

        var users: MutableList<User> = mutableListOf(
            user,
            User(
                "223111892",
                "michalIsovitz@gmail.com", "Michal", "Isovitz",
                19, "female", "0528293088", "MichalIso12"
            )
        )

        var userFavoriteTravelersIds: MutableSet<String> = mutableSetOf("223111892")

        var trips: MutableList<Trip> = mutableListOf(
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

    fun getUser(): User {
        return user
    }

    fun getAllTrips(): MutableList<Trip> {
        return trips
    }

    fun getUserFavoriteTravelers(): MutableList<User> {
        return users.filter { user -> userFavoriteTravelersIds.contains(user.id) }.toMutableList()
    }

    fun removeTravelerFromFavoriteList(id: String): Boolean = userFavoriteTravelersIds.remove(id)

    fun getUserTrips(): MutableList<Trip> {
        return trips.filter { trip -> trip.userName == user.firstName + " " + user.lastName }
            .toMutableList()
    }

    fun addTrip(trip: Trip) {
        trips = (trips + trip).toMutableList()
    }
}