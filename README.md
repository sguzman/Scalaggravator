# ScalAggravator

A UCSB GOLD scraper

![A scala project](https://i.imgur.com/VdpZ4YQ.png)

[![HitCount](http://hits.dwyl.io/sguzman/Scalaggravator.svg)](http://hits.dwyl.io/sguzman/Scalaggravator)
![Travis CI](https://travis-ci.org/sguzman/Scalaggravator.svg?branch=master)
![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bbff53e99a73454aaac14c52a191760e)](https://www.codacy.com/app/guzmansalv/Scalaggravator?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sguzman/Scalaggravator&amp;utm_campaign=Badge_Grade)

### Lessons Learned
Here is a list of lessons I learned while working on this project. These are for
specific consumption; they relate specifically to this project and should be kept
in mind whenever I work on this project. In other words, for posterity.
* .aspx is fickle. Expect failure in communication often
* Do not clean values found in select drop down list.
    * If they need to be submitted, they need to be kept as originally received
* To continue communication, sometimes using the most recent set of cookies is bad
    * Try using cookies of penultimate request instead