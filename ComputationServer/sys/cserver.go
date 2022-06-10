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
	fmt.Println("Server " + version)
	fmt.Println("===================================")
	fmt.Println("")

	packetTypes["0"] = FLYING
	packetTypes["1"] = CLIENT_ABILITIES

	startServer()
}

func startServer() {

	const connectionPort int = 1234

	server, _ := net.Listen("tcp", ":"+strconv.Itoa(connectionPort))

	fmt.Println("listening on ", connectionPort, " . . .")
	fmt.Println("")

	checksList := make([]Check, 1)

	checksList[0] = CheckAbilties{}

	// run loop forever (or until ctrl-c)
	for {

		// accept connection
		socket, _ := server.Accept()

		// get message, output
		message, _ := bufio.NewReader(socket).ReadString('\n')

		executeChecks(message, checksList)
	}
}

func executeChecks(message string, checksList []Check) {
	for i := 0; i < len(checksList); i++ {

		args := strings.Split(message, "|")

		checksList[i].OnPacketReceived(packetTypes[args[0]], args)
	}
}
