#Sm:)e
CS + Mental Health Android app built during TreeHacks 2016.  
Team members: Kristen Law, Catherina Xu, Grace Young, Meera Srinivasan  
http://devpost.com/software/sm-e

##Inspiration
It's easy to feel overwhelmed in college. According to Psychology Today, over 50% of college students rated their mental health as "below average" or "poor." We know how difficult it is to take a step back from the inevitable onslaught of PSETs, social commitments, and extracurricular activities, so we wanted to make mental health resources more accessible, personalized, and fun. Journaling, music, calming visuals, and awareness of emotional trends are all clinically proven emotional boosters that are easily modeled by technology. These are just a few of the features we worked into Sm:)e - we have many more on the way!

##What it does
Sm:)e keeps you smiling by using data to keep your mental health in check. By analyzing the tone of your most recent texts, keeping track of your mood, and promoting auditory and visual relaxation, Sm:)e presents a stunningly simple and beautiful way to be at your mental best.

##How we built it
We extracted texts from the user's phone by using Android Telephony. These texts were concatenated and analyzed using IBM Watson's Tone Analyzer Java API, which returned emotional, writing, and social tones for each set of texts. We then used MPAndroidChart to create radar charts of the aggregated data over different periods of time. Users are able to share their results with their healthcare providers, and directly call Stanford CAPS (Psychological Services) through the app.

Another facet of our project involved creating a diary of the user's self-reported moods. The interactive survey method allowed users to select a range of different moods from a Pinterest-style menu (RecycleView and CardView, and the results were recorded in a ListView).

Lastly, we focused on improving the user experience and creating actionables for immediate relief from stress and other mental health related challenges. We used Android's native media player and utilized multi-threaded processes to deliver a meditative auditory experience. This, coupled with animated, soothing images, are designed to deepen the level of relaxation.
