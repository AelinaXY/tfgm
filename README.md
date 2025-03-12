# TFGM Simulator

This application, in conjunction with the tfgm-frontend repository, provides an up to date map of where individual trams are in the tram network aswell as plotting your best route to your destination, including times and breaks between each tram.



## Next Steps
### 1) Tram History Processing
The next thing that needs to happen to this application is enhanced tram history processing.
 -[x] Add Tram History table
 -[x] Populate Tram History Live
 - [ ] Create a scheduled executor to process Tram History and update JourneyTime based on results

### 2) People Processing
After Tram History is created and optimised, People are the next order of business. By adding people and processing them correctly the end goal is that you can view live tram data and see, roughly, how many people wil be on a tram. To do this, several steps will need to be followed.
- [ ] Firstly, we must generate people at regular intervals
- [ ] Secondly, those people must be assigned to journeys
- [ ] Thirdly, those journeys must be collated so that a specific journey at a specific time will have a specific number of people
- [ ] Fourthly, this will then be added to a database that stores a time window, a journey, and an amount of people
- [ ] Finally, this will be added to live tram data as a percent of max and bubbled through to the front end as both live and historic data.