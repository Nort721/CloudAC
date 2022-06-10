package sys

import "fmt"

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

	for i := 0; i < len(args); i++ {
		fmt.Println(args[i])
	}

	if packetType == JOIN {

		if !Contains(uuid) {
			Add(uuid)
			fmt.Println("New profile has been created uuid -> ", uuid)
		}

	} else if packetType == LEAVE {

		Remove(uuid)
		fmt.Println("Profile has been removed uuid -> ", uuid)

	} else if packetType == FLYING {

		if Contains(uuid) {
			profile := Get(uuid)

			profile.x = 0
			profile.y = 0
			profile.z = 0
			profile.yaw = 0
			profile.pitch = 0
		}

	} else if packetType == CLIENT_ABILITIES {

		var isFlying string = args[3]
		var isAllowedFlight string = args[4]

		if Contains(uuid) {
			profile := Get(uuid)

			profile.isAllowedFlightClient = isFlying == "true"
			profile.isAllowedFlightClient = isAllowedFlight == "true"
		}

	} else if packetType == SERVER_ABILITIES {

		var isFlying string = args[3]
		var isAllowedFlight string = args[4]

		if Contains(uuid) {
			profile := Get(uuid)

			profile.isAllowedFlightServer = isFlying == "true"
			profile.isAllowedFlightServer = isAllowedFlight == "true"
		}

	}
}
