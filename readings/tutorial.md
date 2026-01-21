











# Clients

### AdapterClient

AdapterClient is a standard client instance that you can add your own listeners to. 
This library uses [MCProtocolLib](https://github.com/GeyserMC/MCProtocolLib) SessionAdapters,
so for more information refer to their documentation.  


## SimpleClients
...are preconfigured AdapterClients that you can use instead of writing your own one.

### CommandTextLoggerClient

Sends an array of commands with programmable delay in between and logs DisugisedChatPackets performing lambda checks on their content. 

For most use-cases time timeout should be less than 1s after last command.