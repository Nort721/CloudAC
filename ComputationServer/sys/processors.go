package sys

import (
	"fmt"
	"strconv"
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

		x, _ := strconv.ParseFloat(args[6], 64)
		y, _ := strconv.ParseFloat(args[7], 64)
		z, _ := strconv.ParseFloat(args[8], 64)
		yaw, _ := strconv.ParseFloat(args[9], 32)
		pitch, _ := strconv.ParseFloat(args[10], 32)
		isPos, _ := strconv.ParseBool(args[3])
		isLook, _ := strconv.ParseBool(args[4])
		onGround, _ := strconv.ParseBool(args[5])

		if Contains(uuid) {
			profile := Get(uuid)

			profile.lastOnGround = profile.onGround
			profile.onGround = onGround

			if isPos {
				profile.lastLocation.x = profile.latestLocation.x
				profile.lastLocation.y = profile.latestLocation.y
				profile.lastLocation.z = profile.latestLocation.z

				profile.latestLocation.x = x
				profile.latestLocation.y = y
				profile.latestLocation.z = z
			}

			if isLook {
				profile.lastLocation.yaw = profile.latestLocation.yaw
				profile.lastLocation.pitch = profile.latestLocation.pitch

				profile.latestLocation.yaw = float32(yaw)
				profile.latestLocation.pitch = float32(pitch)
			}

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
