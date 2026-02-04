"""
Unit tests for the Agentic AI system.
"""
import pytest
import asyncio
from app.agents.imaging_agent import ImagingAgent
from app.agents.genomic_agent import GenomicAgent
from app.agents.clinical_agent import ClinicalAgent
from app.agents.orchestrator import OrchestratorAgent


@pytest.mark.asyncio
async def test_imaging_agent():
    """Test ImagingAgent analysis."""
    agent = ImagingAgent()
    
    test_data = {
        "image_data": "test_image_base64",
        "modality": "mammography"
    }
    
    result = await agent.analyze(test_data)
    
    assert "prediction" in result
    assert "confidence" in result
    assert result["agent"] == "ImagingAgent"
    assert result["prediction"] in [0, 1]
    assert 0 <= result["confidence"] <= 1


@pytest.mark.asyncio
async def test_genomic_agent():
    """Test GenomicAgent analysis."""
    agent = GenomicAgent()
    
    test_data = {
        "gene_expressions": {"BRCA1": 5.2, "BRCA2": 3.1},
        "mutations": []
    }
    
    result = await agent.analyze(test_data)
    
    assert "prediction" in result
    assert "confidence" in result
    assert result["agent"] == "GenomicAgent"
    assert result["prediction"] in [0, 1]


@pytest.mark.asyncio
async def test_clinical_agent():
    """Test ClinicalAgent analysis."""
    agent = ClinicalAgent()
    
    test_data = {
        "age": 55,
        "family_history": True,
        "bmi": 26.5,
        "menopause_status": "post"
    }
    
    result = await agent.analyze(test_data)
    
    assert "prediction" in result
    assert "confidence" in result
    assert result["agent"] == "ClinicalAgent"
    assert "risk_factors" in result


@pytest.mark.asyncio
async def test_orchestrator():
    """Test OrchestratorAgent coordination."""
    orchestrator = OrchestratorAgent()
    
    test_data = {
        "patient_id": "TEST001",
        "imaging_data": {
            "image_data": "test_image",
            "modality": "mammography"
        },
        "genomic_data": {
            "gene_expressions": {"BRCA1": 5.2}
        },
        "clinical_data": {
            "age": 55,
            "family_history": True
        }
    }
    
    result = await orchestrator.diagnose(test_data)
    
    assert "diagnosis_summary" in result
    assert "agent_analyses" in result
    assert "recommendations" in result
    assert "interpretation" in result
    assert result["patient_id"] == "TEST001"


@pytest.mark.asyncio
async def test_ensemble_decision():
    """Test ensemble decision making with multiple agents."""
    orchestrator = OrchestratorAgent()
    
    # Test with all three data types
    comprehensive_data = {
        "patient_id": "TEST002",
        "imaging_data": {"image_data": "test", "modality": "mammography"},
        "genomic_data": {"gene_expressions": {}},
        "clinical_data": {"age": 45, "family_history": False}
    }
    
    result = await orchestrator.diagnose(comprehensive_data)
    
    # Check that all agents were consulted
    assert len(result["agent_analyses"]) == 3
    assert "imaging" in result["agent_analyses"]
    assert "genomic" in result["agent_analyses"]
    assert "clinical" in result["agent_analyses"]
    
    # Check consensus
    assert "consensus_level" in result["diagnosis_summary"]
    assert result["diagnosis_summary"]["consensus_level"] in ["unanimous", "strong", "weak", "none"]


if __name__ == "__main__":
    pytest.main([__file__, "-v"])
