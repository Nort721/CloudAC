package sys

type PacketType int64

const (
	JOIN             PacketType = 0
	LEAVE            PacketType = 1
	FLYING           PacketType = 2
	CLIENT_ABILITIES PacketType = 3
	SERVER_ABILITIES PacketType = 4
)
