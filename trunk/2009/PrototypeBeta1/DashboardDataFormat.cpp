
#include "DashboardDataFormat.h"

DashboardDataFormat::DashboardDataFormat(void)
	: m_ds (DriverStation::GetInstance())
{
	memset(m_AnalogChannels[0], 0, sizeof(m_AnalogChannels[0]));
	memset(m_AnalogChannels[1], 0, sizeof(m_AnalogChannels[1]));
	memset(m_PWMChannels[0], 128, sizeof(m_PWMChannels[0]));
	memset(m_PWMChannels[1], 128, sizeof(m_PWMChannels[1]));
	memset(m_RelayFwd, 0, sizeof(m_RelayFwd));
	memset(m_RelayRev, 0, sizeof(m_RelayRev));
	memset(m_DIOChannels, 0, sizeof(m_DIOChannels));
	memset(m_DIOChannelsOutputEnable, 0, sizeof(m_DIOChannelsOutputEnable));
	m_SolenoidChannels = 0;
}

DashboardDataFormat::~DashboardDataFormat()
{
	
}

/**
 * Pack data using the correct types and in the correct order to match the
 * default "Dashboard Datatype" in the LabVIEW Dashboard project.
 */
void DashboardDataFormat::PackAndSend(void)
{
	Dashboard &dashboardPacker = m_ds->GetDashboardPacker();
	UINT32 module;
	UINT32 channel;

	// Pack the analog modules
	for (module = 0; module < kAnalogModules; module++)
	{
		dashboardPacker.AddCluster();
		for (channel = 0; channel < kAnalogChannels; channel++)
		{
			dashboardPacker.AddFloat(m_AnalogChannels[module][channel]);
		}
		dashboardPacker.FinalizeCluster();
	}
	// Pack the digital modules
	for (module = 0; module < kDigitalModules; module++)
	{
		dashboardPacker.AddCluster();
		dashboardPacker.AddU8(m_RelayFwd[module]);
		dashboardPacker.AddU8(m_RelayRev[module]);
		dashboardPacker.AddU16(m_DIOChannels[module]);
		dashboardPacker.AddU16(m_DIOChannelsOutputEnable[module]);
		dashboardPacker.AddCluster();
		for(channel = 0; channel < kPwmChannels; channel++)
		{
			dashboardPacker.AddU8(m_PWMChannels[module][channel]);
		}
		dashboardPacker.FinalizeCluster();
		dashboardPacker.FinalizeCluster();
	}
	// Pack the solenoid module
	dashboardPacker.AddU8(m_SolenoidChannels);

	// Flush the data to the driver station.
	dashboardPacker.Finalize();
}
