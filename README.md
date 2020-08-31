# Bike Week 2020 Web Generator
The code that generates a large part of the Madison Bikes Bike Week calendar and map data.

# Updating
Execute the following command:
```
./gradlew calendar upload -P madisonBikesWebUsername=<username> -P madisonBikesWebPassword=<password>
```

# Details of tasks performed
1. Export the data from main sheet at [2020 Master List](https://docs.google.com/spreadsheets/d/19ils5BDZpkBe00H8wsQ2Cj1ANb9ib27iLVbho0k7aeg/edit#gid=0) to a CSV and store in ```events.csv``` in this repository.
1. Run task (org.madisonbikes.bikeweek.ProcessEventsTask) with the following arguments "events.csv build/output" and several files will be created in the ```build/output``` folder.
1. Run pandoc command (in gradle)
1. Upload HTML to dreamhost
