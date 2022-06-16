package sys

import (
	"bufio"
	"fmt"
	"net"
	"strconv"
	"strings"
)

var packetTypes = make(map[string]PacketType)

func Start() {

	const version string = "Build 0"

	fmt.Println("===================================")
	fmt.Println("AntiCheat-ComputationServer " + version)
	fmt.Println("===================================")
	fmt.Println("")

	packetTypes["0"] = JOIN
	packetTypes["1"] = LEAVE
	packetTypes["2"] = FLYING
	packetTypes["3"] = CLIENT_ABILITIES
	packetTypes["4"] = SERVER_ABILITIES

	startServer()
}

func startServer() {

	const connectionPort int = 1234

	server, _ := net.Listen("tcp", ":"+strconv.Itoa(connectionPort))

	fmt.Println("listening on", connectionPort, " . . .")
	fmt.Println("")

	checksList := make([]Check, 2)
	processorsList := make([]Processor, 1)

	checksList[0] = CheckAbilties{}
	checksList[1] = CheckSpeed{}
	processorsList[0] = StatusProcessor{}

	// run loop forever (or until ctrl-c)
	for {

		// accept connection
		socket, _ := server.Accept()

		// get message, output
		message, _ := bufio.NewReader(socket).ReadString('\n')

		message = Decode(message)

		// remove the \n at the end
		message = strings.Trim(message, "\n")

		//fmt.Println("received request ->", message)

		executeProcessors(message, processorsList)
		executeChecks(message, checksList)
	}
}

func executeProcessors(message string, processeorsList []Processor) {
	for i := 0; i < len(processeorsList); i++ {

		args := strings.Split(message, "|")

		processeorsList[i].ProcessesIncomingPacket(packetTypes[args[0]], args)
	}
}

func executeChecks(message string, checksList []Check) {
	for i := 0; i < len(checksList); i++ {

		args := strings.Split(message, "|")

		checksList[i].OnPacketReceived(packetTypes[args[0]], args)
	}
}
