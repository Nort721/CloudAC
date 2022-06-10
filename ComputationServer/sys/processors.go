package sys

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

		// preventing doubles
		if !profiles.contains(uuid) {
			profiles.Add(uuid)
		}

	} else if packetType == LEAVE {
		profiles.Remove(uuid)
	} else if packetType == SERVER_ABILITIES {

		var isAllowedFlight string = args[4]

		if profiles.contains(uuid) {
			profiles.get(uuid).isAllowedFlight = (isAllowedFlight == "canfly=true")
		}
	}
}
