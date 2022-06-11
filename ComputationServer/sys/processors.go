package sys

import (
	"fmt"
	"strings"
)

type Processor interface {
	ProcessesIncomingPacket(packetType PacketType, args []string)
}

func ProcessesIncomingPacket(s Processor, packetType PacketType, data []string) {
	s.ProcessesIncomingPacket(packetType, data)
}

type StatusProcessor struct {
	Text string
}

func (c StatusProcessor) ProcessesIncomingPacket(packetType PacketType, args []string) {
	var uuid string = args[1]

	if packetType == JOIN {

		if !Contains(uuid) {
			Add(uuid)
			fmt.Println("server -> New profile (uuid-" + uuid + ")")
		}

	} else if packetType == LEAVE {

		Remove(uuid)
		fmt.Println("server -> Removed profile (uuid-" + uuid + ")")

	} else if packetType == FLYING {

		if Contains(uuid) {
			profile := Get(uuid)

			profile.x = 0
			profile.y = 0
			profile.z = 0
			profile.yaw = 0
			profile.pitch = 0

			profile_map[uuid] = profile
		}

	} else if packetType == CLIENT_ABILITIES {

		var isFlying string = args[3]
		var isAllowedFlight string = args[4]

		if Contains(uuid) {
			profile := Get(uuid)

			// there is probably a better way to do that, but works for now...
			profile.isFlyingClient = strings.Contains(isFlying, "true")
			profile.isAllowedFlightClient = strings.Contains(isAllowedFlight, "true")

			profile_map[uuid] = profile
		}

	} else if packetType == SERVER_ABILITIES {

		var isFlying string = args[3]
		var isAllowedFlight string = args[4]

		if Contains(uuid) {
			profile := Get(uuid)

			// there is probably a better way to do that, but works for now...
			profile.isFlyingServer = strings.Contains(isFlying, "true")
			profile.isAllowedFlightServer = strings.Contains(isAllowedFlight, "true")

			profile_map[uuid] = profile
		}

	}
}
