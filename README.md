# What is CloudAC
CloudAC is a prototype, proof of concept, micro Minecraft anti-cheat system that performs all its checks and data processing on a separate program that can be installed on a different machine to truly remove all the performance impact of the checks and data processors (Which are the heaviest parts of an anti-cheat) from the Minecraft-server itself, the only anti-cheat related actions that are actually performed by the Minecraft-server's resources is sending all the relevant packets data to the Computation server and the punishment system.

### Why
CloudAC shows an idea that would work to actually completely solve one of the biggest problems of server-sided Minecraft anti-cheats, agree or not any AntiCheat even with the best optimizations and most elegant source code that's built to prevent most or all the game-breaking cheats in Minecraft will take significantly more resources than your average plugin, sometimes in more severe cases, the AntiCheat will improve your player's experience by banning cheaters but will also degrade it because of lag caused by it.

### Technical details
CloudAC's current design has two parts, the ACTransmitter and the ComputationServer, the ACTransmitter sends all the required data to the ComputationServer as soon as it arrived and the ComputationServer processes this data and uses it to run its checks then sends the results of these checks back to the ACTransmitter where it's used in the punishment system, the computation server always tracks what happens on the server and uses most of the design choices and tactics that are already used by any traditional AntiCheat, keep in mind that this explanation is simplified. 

#### Technical details - More choices
Another advantage of having the ComputationServer as a separate program is that we can program it in whatever programming language we want, in this case, I used Go because it is compiled, modern, a little faster than java, and is functional oriented but still has stuff like interfaces so you can still kind of re-create your AntiCheat polymorphic design even though the language is functionaly oriented, and I already used it before, however in theory you can write it really in whatever language you want, another good choice would be Rust since its modern has a big amount of features and its great performance.

### What about the latency
In CloudAC's case, the latency of sending the packets data to the Computation program using sockets is not much of a problem as we are going for a silent AntiCheat design, there are two ways to configure an AntiCheat to operate depending on what your priority is, banning the cheater, or preventing the cheat, if your priority is to ban them, its better to not let the cheater know if he is being detected by setting him back, it's better to "keep them in the dark" and let them cheat until your AntiCheat has enough data to ban them, this is the tactic most bigger servers use and is the one we chose to go with for CloudAC.

### Contributing
CloudAC is a prototype meant to show how designing this kind of system could work and was never meant to be anything more than that, however we are open-minded and while without dedicated development it would probably never become a production ready anti-cheat we do welcome any contributions that come to expand and improve the code base which will provide a better example for the community.

If you are interested in helping us continute and improve CloudAC,
here is a ToDo list:
- Upgrade the sockets code to use a secure encrypted connection with TLS
- Improve data processing to be dynamic and not need to check packet type
- Add more checks and increase data processing
- Your suggestions

### Disclaimer
I didn't invent the idea of running the brain of your AntiCheat separately from your Minecraft-server itself, that idea existed for some time and is/was used by a few big servers, the idea of CloudAC is to make it more widespread and known to the public and hopefully inspire future projects to use this superior design and by that hopefully improve the AntiCheating solutions that are coming out there.

#### Disclaimer - current code state
The current code was written pretty swiftly and was bodged together to some extent since originally I didn't plan on publishing it, it was meant to be more of a proof of concept attempt, which is why some parts might be a bit wack, I did and will introduce more clean up and comments but I believe it is decent already and can definitely be used as a nice base, however, feel free to implement any changes or improvements you believe fit.
<br/><br/>
<br/><br/>
![resized](https://user-images.githubusercontent.com/24839815/174480405-35d2422c-f1b8-4035-a7c2-ff34a2cfb89a.png)
