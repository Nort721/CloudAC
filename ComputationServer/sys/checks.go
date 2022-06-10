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
	if packetType == CLIENT_ABILITIES {
		for i := 0; i < len(args); i++ {
			fmt.Println(args[i])
		}

		if args[3] == "isflying=true" {
			fmt.Println("FLAG!")
		}
	}
}
