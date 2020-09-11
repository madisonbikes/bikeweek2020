# Bike Week 2020 Web Generator
The code that generates a large part of the Madison Bikes Bike Week calendar and map data.

# Updating
Execute the following commands:
```
# downloads CSV and generates HTML
./gradlew calendar

# uploads to MB web site
./gradlew upload -P madisonBikesWebUsername=<username> -P madisonBikesWebPassword=<password>
```

Before uploading to the server, it's a good idea to run the `calendar` task alone in order to verify that the generated html looks right. Compare using git diff.

# Details of tasks performed
1. Export the data from main sheet at [2020 Master List](https://docs.google.com/spreadsheets/d/19ils5BDZpkBe00H8wsQ2Cj1ANb9ib27iLVbho0k7aeg/edit#gid=0) to a CSV and store in ```data/events.csv``` in this repository.
1. Run Java tool ```org.madisonbikes.bikeweek.ProcessEventsTask```, generating markdown, etc.
1. Run `pandoc` command as detailed in the gradle file, converting markdown to html with toc, etc.
1. Upload HTML to dreamhost using `curl`

# Requirements
You need `pandoc` installed. That's available on most Linux platforms and on OS X using Homebrew: `brew install pandoc` command.

In order to upload the file, you'll need a version of `curl` that supports sftp protocol. This should be the
default on Linux, on OS X you can install using Homebrew: `brew install marcelomazza/homebrew-curl-libssh2/curl`
