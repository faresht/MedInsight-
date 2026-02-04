# MedInsight+ AI Service

Agentic AI system for breast cancer diagnosis using multimodal data (imaging, genomic, and clinical).

## Architecture

This service implements a **Hybrid Agentic AI Architecture** with **Late Fusion (Ensemble)** approach:

### Specialized Agents

1. **ImagingAgent**: Analyzes medical images (mammography, MRI, ultrasound)
   - Feature extraction from medical images
   - Detection of masses, microcalcifications, architectural distortions
   - Confidence threshold: 0.6

2. **GenomicAgent**: Analyzes genetic and molecular data
   - BRCA1/BRCA2 mutation detection
   - Gene expression analysis
   - Receptor status (ER, PR, HER2)
   - Confidence threshold: 0.65

3. **ClinicalAgent**: Analyzes patient clinical information
   - Age and family history risk assessment
   - Lifestyle factors analysis
   - Hormonal and reproductive factors
   - Confidence threshold: 0.55

4. **OrchestratorAgent**: Coordinates all agents and makes final decision
   - Weighted ensemble decision-making
   - Consensus calculation
   - Comprehensive report generation

## Features

- ✅ Multimodal data fusion
- ✅ Interpretable AI with explainable predictions
- ✅ Risk scoring (0-100%)
- ✅ Clinical recommendations
- ✅ RESTful API with FastAPI
- ✅ Comprehensive logging
- ✅ Docker support

## Installation

### Prerequisites

- Python 3.11+
- pip

### Local Development

```bash
# Create virtual environment
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Run the service
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

### Docker

```bash
# Build the image
docker build -t medinsight-ai-service .

# Run the container
docker run -p 8000:8000 medinsight-ai-service
```

## API Endpoints

### Health Check
```http
GET /health
```

### Diagnose Breast Cancer
```http
POST /api/v1/diagnose
Content-Type: application/json

{
  "patient_id": "P12345",
  "imaging_data": {
    "image_data": "base64_encoded_image",
    "modality": "mammography"
  },
  "genomic_data": {
    "gene_expressions": {"BRCA1": 5.2, "BRCA2": 3.1},
    "mutations": []
  },
  "clinical_data": {
    "age": 55,
    "family_history": true,
    "bmi": 26.5,
    "menopause_status": "post"
  }
}
```

### List Agents
```http
GET /api/v1/agents
```

## API Documentation

Once the service is running, visit:
- Swagger UI: http://localhost:8000/docs
- ReDoc: http://localhost:8000/redoc

## Example Usage

```python
import requests

# Prepare diagnosis request
data = {
    "patient_id": "P12345",
    "clinical_data": {
        "age": 55,
        "family_history": True,
        "bmi": 26.5,
        "menopause_status": "post",
        "previous_biopsies": 1
    },
    "genomic_data": {
        "gene_expressions": {"BRCA1": 5.2, "BRCA2": 3.1}
    }
}

# Send request
response = requests.post("http://localhost:8000/api/v1/diagnose", json=data)
result = response.json()

# Access results
print(f"Diagnosis: {result['diagnosis_summary']['final_diagnosis']}")
print(f"Risk Score: {result['diagnosis_summary']['risk_score']:.1f}%")
print(f"Recommendations: {result['recommendations']}")
```

## Response Structure

```json
{
  "patient_id": "P12345",
  "diagnosis_summary": {
    "final_diagnosis": "benign - high confidence",
    "risk_score": 35.2,
    "confidence": 0.82,
    "consensus_level": "strong",
    "agent_agreement_percentage": 100.0
  },
  "agent_analyses": {
    "imaging": {...},
    "genomic": {...},
    "clinical": {...}
  },
  "key_findings": [...],
  "recommendations": [...],
  "interpretation": "..."
}
```

## Testing

```bash
# Run tests
pytest tests/ -v

# Run with coverage
pytest tests/ --cov=app --cov-report=html
```

## Integration with MedInsight+ Backend

This AI service integrates with the main Spring Boot backend through the Analytics Service:

1. Analytics Service calls this AI service via REST API
2. Results are stored in MongoDB
3. Frontend displays results through Analytics Service endpoints

## Technology Stack

- **Framework**: FastAPI
- **ML Libraries**: PyTorch, TensorFlow, Scikit-learn
- **Interpretability**: SHAP, LIME
- **Medical Imaging**: PyDICOM, SimpleITK
- **API Documentation**: OpenAPI/Swagger

## Future Enhancements

- [ ] Real CNN models for imaging analysis
- [ ] SHAP/LIME integration for detailed interpretability
- [ ] Model training pipeline
- [ ] Performance optimization with model caching
- [ ] Batch processing support
- [ ] WebSocket support for real-time updates

## License

Part of the MedInsight+ E-Health Platform
