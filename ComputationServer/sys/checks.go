package sys

import "fmt"

type Check interface {
	OnPacketReceived(packetType PacketType, args []string)
}

func OnPacketReceived(c Check, packetType PacketType, data []string) {
	c.OnPacketReceived(packetType, data)
}

type CheckAbilties struct {
	Text string
}

func (c CheckAbilties) OnPacketReceived(packetType PacketType, args []string) {
	var uuid string = args[1]

	for i := 0; i < len(args); i++ {
		fmt.Println(args[i])
	}

	if packetType == CLIENT_ABILITIES {

		profile := Get(uuid)
		if args[3] == "true" && !profile.isAllowedFlightServer {
			fmt.Println("FLAG!")
		}

	}
}
