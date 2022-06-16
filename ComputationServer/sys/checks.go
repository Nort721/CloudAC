package sys

import (
	"fmt"
	"net"
)

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

	if packetType == CLIENT_ABILITIES {

		profile := Get(uuid)

		if profile.isAllowedFlightClient && !profile.isAllowedFlightServer {
			flagPlayer(uuid, "CheckAbilties")
		}

	}
}

type CheckSpeed struct {
	Text string
}

func (c CheckSpeed) OnPacketReceived(packetType PacketType, args []string) {
	var uuid string = args[1]

	if packetType == FLYING {

		profile := Get(uuid)

		var from Location = profile.lastLocation
		var to Location = profile.latestLocation

		var offsetX float64 = to.x - from.x
		var offsetZ float64 = to.x - from.z

		var offsetHSquared float64 = offsetX*offsetX + offsetZ*offsetZ

		// friction in air
		var friction float64 = 0.91

		var shiftedLastDist float64 = profile.speedData.lastOffsetH * friction

		var equal float64 = offsetHSquared - shiftedLastDist
		var scaledEqual float64 = equal * 138

		vl := profile.speedData.vl

		if !profile.onGround && !profile.lastOnGround {

			fmt.Println("scaledEqual:", scaledEqual)
			if scaledEqual > 1.1 {
				fmt.Println("vl:", vl)
				vl += 1
				if vl > 2 {
					vl = 0
					flagPlayer(uuid, "CheckSpeed")
				}
			} else {
				vl = 0
			}
		}

		profile.speedData.vl = vl

		profile.speedData.lastOffsetH = offsetHSquared

	}
}

func flagPlayer(uuid string, data string) {
	fmt.Println("server -> " + data + " -> FLAG!")

	conn, _ := net.Dial("tcp", "localhost:1212")

	var message string = "FLAG|" + uuid + "|" + data

	fmt.Fprintf(conn, message+"\n")
}
